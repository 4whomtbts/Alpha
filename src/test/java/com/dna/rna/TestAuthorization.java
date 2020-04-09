package com.dna.rna;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class TestAuthorization {

    @Autowired
    protected MockMvc mockMvc;

    protected String token;

    @Before
    public void retrieveToken() throws Exception {

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "jun");
        requestBody.put("password", "happy");
        String requestBodyString = requestBody.toString();

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyString)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        this.token = mvcResult.getResponse().getHeader("Authorization");
        if (this.token == null) {
            throw new NullPointerException("retrieved token is null");
        }
    }

}
