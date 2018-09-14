package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.config.ApplicationProperties;
import org.mlccc.cm.config.WebConfigurer;
import org.mlccc.cm.domain.NewsLetter;
import org.mlccc.cm.domain.User;
import org.mlccc.cm.service.NewsLetterService;
import org.mlccc.cm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.time.LocalDate;


/**
 * REST controller for managing fileupload.
 */
@RestController
@RequestMapping("/api")
public class FileUploadResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    @Autowired
    private final NewsLetterService newsLetterService;

    @Autowired
    private final UserService userService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public FileUploadResource(NewsLetterService newsLetterService, UserService userService) {
        this.newsLetterService = newsLetterService;
        this.userService = userService;
    }

    /**
     * POST  /fileupload  upload a file
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new classRoom, or with status 400 (Bad Request) if the classRoom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fileupload")
    @Timed
    public ResponseEntity create(@RequestParam (required = true) String type, MultipartFile file) throws URISyntaxException {
        log.debug("REST request to upload file : {}", file.getOriginalFilename());

        User loginUser = userService.getUserWithAuthorities();

        if (!file.isEmpty()) {
            try {
                File directory = null;
                String newFileName = file.getOriginalFilename();

                if(type.equals("NewsLetter")){
                    String newsletterUploadDir = applicationProperties.getNewsletterUploadDir();
                    directory  = new File(newsletterUploadDir);
                } else {
                    // other directories
                }

                if(!directory.exists() || !directory.isDirectory()){
                    if (!directory.mkdir()) {
                        return ResponseEntity.badRequest().header("Failure", "Directory doesn't not exist, and can't be created").body(null);
                    }
                }

                // create file
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(directory +"/"+ newFileName)));
                stream.write(bytes);
                stream.close();

                // create a newsletter
                if(type.equals("NewsLetter")){
                    // create a newsletter
                    NewsLetter newsLetter = new NewsLetter();
                    newsLetter.setName(newFileName);
                    newsLetter.setFileName(newFileName);
                    newsLetter.setUploadDate(LocalDate.now());
                    //newsLetter.setUploadedBy(loginUser.getId());
                    newsLetterService.save(newsLetter);
                } else {
                    // other upload types
                }
                return ResponseEntity.created(new URI("/api/fileupload")).body("{\"status\": \"done\"}");
            } catch (Exception e) {
                return ResponseEntity.badRequest().header("Failure", "File upload failed").body("{\"status\": \"error\"}");
            }
        } else {
            return ResponseEntity.badRequest().header("Failure", "Uploaded file is empty").body("{\"status\": \"error\"}");
        }
    }
}
