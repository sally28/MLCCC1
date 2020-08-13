package org.mlccc.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mlccc.cm.domain.Teacher;
import org.mlccc.cm.domain.User;
import org.mlccc.cm.service.MailService;
import org.mlccc.cm.service.TeacherService;
import org.mlccc.cm.service.UserService;
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

    @Autowired
    private final UserService userService;

    public EmailResource(MailService mailService, TeacherService teacherService, UserService userService) {
        this.mailService = mailService;
        this.teacherService = teacherService;
        this.userService = userService;
    }

    @PostMapping("/email")
    @Timed
    public ResponseEntity send(@RequestParam (required = true) String recipients, @RequestParam (required = true) String subject,
                               @RequestParam (required = true) String content, @RequestParam (required = false) String cc,
                               @RequestParam (required = false) String bcc ) throws URISyntaxException {
        log.debug("REST request to send email : {}", recipients);
        String[] to;
        String[] ccList;
        String[] bccList;
        StringBuffer emails = new StringBuffer();
        ccList = (cc != null)? cc.split(",") : null;
        bccList = (bcc != null)? bcc.split(",") : null;
        if(recipients.equals("All Teachers")){
            List<Teacher> teachers = teacherService.findAll();
            for(Teacher teacher : teachers){
                if(teacher.getAccount() != null){
                    emails.append(teacher.getAccount().getEmail()).append(",");
                }
            }
            to = "mlcccwndschool@gmail.com".split(",");
            bccList = emails.toString().split(",");
        } else if(recipients.equals("All Users")) {
            to = "principal@mlccc.org".split(",");
            List<User> allUsers = userService.getAllActiveUsers();
            for(User user : allUsers){
                if(!user.getEmail().isEmpty()){
                    emails.append(user.getEmail()).append(",");
                }
            }
            bccList = emails.toString().split(",");
        } else {
            to = recipients.split(",");
        }

        mailService.sendEmail(to, subject, content, ccList, bccList, false, false);

        return ResponseEntity.created(new URI("/api/account-email/"))
                .headers(HeaderUtil.emailSentAlert(recipients))
                .body("{\"status\": \"done\"}");
    }
}

