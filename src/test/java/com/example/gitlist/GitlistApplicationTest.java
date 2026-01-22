package com.example.gitlist;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8080)
class GitlistApplicationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        System.setProperty("github.api.url", "http://localhost:8081");
    }

    @Test
    void ListRepositoryTest() throws Exception {
        String username = "jruszer";

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/" + username + "/repos"))
                .willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\": \"repo1\", \"owner\": {\"login\": \"test-user\"}, \"fork\": false}]")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/repos/" + username + "/repo1/branches"))
                .willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\": \"main\", \"commit\": {\"sha\": \"123\"}}]")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/github/repos/" + username).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].repositoryName", is("List-github-repository-api")))
                .andExpect(jsonPath("$[1].ownerLogin", is("jruszer")))
                .andExpect(jsonPath("$[1].branches[0].name", is("main")))
                .andExpect(jsonPath("$[1].branches[0].lastCommitSha", is("bec9fab39931a4a014ed146103e23cfedcbd14f3")));
    }

    @Test
    void userNotFoundTest() throws Exception {
        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/users/a1214654/repos")).willReturn(WireMock.aResponse().withStatus(404)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/github/repos/a1214654")).andExpect(status().isNotFound());
    }
}