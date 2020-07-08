package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.Teacher;
import org.mlccc.cm.service.MailService;
import org.mlccc.cm.service.TeacherService;
import org.mlccc.cm.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


/**
 * REST controller for managing fileupload.
 */
@RestController
@RequestMapping("/api")
public class EmailResource {

    private final Logger log = LoggerFactory.getLogger(EmailResource.class);

    @Autowired
    private final MailService mailService;

    @Autowired
    private final TeacherService teacherService;

    public EmailResource(MailService mailService, TeacherService teacherService) {
        this.mailService = mailService;
        this.teacherService = teacherService;
    }

    @PostMapping("/email")
    @Timed
    public ResponseEntity send(@RequestParam (required = true) String recipients, @RequestParam (required = true) String subject,
                               @RequestParam (required = true) String content) throws URISyntaxException {
        log.debug("REST request to send email : {}", recipients);
        String[] to;
        StringBuffer emails = new StringBuffer();
        if(recipients.equals("All Teachers")){
            List<Teacher> teachers = teacherService.findAll();
            for(Teacher teacher : teachers){
                if(teacher.getAccount() != null){
                    emails.append(teacher.getAccount().getEmail()).append(",");
                }
            }
            to = emails.toString().split(",");
        } else {
            to = recipients.split(",");
        }

        //mailService.sendEmail(to, subject, content, false, false);

        return ResponseEntity.created(new URI("/api/account-email/"))
                .headers(HeaderUtil.emailSentAlert(recipients))
                .body("{\"status\": \"done\"}");
    }
}

