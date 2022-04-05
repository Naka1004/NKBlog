package com.blog.controller;

import com.blog.domain.impl.loginDAO;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api", method = RequestMethod.GET)
public class APIController {

    private static final Logger log = LoggerFactory.getLogger(APIController.class);
    loginDAO ld = new loginDAO();
    private static Gson gs = new Gson();

    @GetMapping(value = "user/{id}", produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> getUser(@PathVariable("id") String uid) throws Exception {
        if (uid.equals("all")) {
            return new ResponseEntity<>(gs.toJson(ld.getAllAccountData()), HttpStatus.OK);
        } else if (ld.getAccountData(uid) != null) {
            return new ResponseEntity<>(gs.toJson(ld.getAccountData(uid)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value= "user/{id}/{password}/{password2}", produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> setPassword(@PathVariable("id") String id, @PathVariable("password") String password
            , @PathVariable("password2") String password2, HttpServletResponse response) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (ld.setPassword(id, password, password2)) {
            sb.append(id + "님의 패스워드를 ");
            sb.append(password2 + "으로 변경 하였습니다.");
            log.debug(sb.toString());
            return new ResponseEntity<>(sb.toString(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
