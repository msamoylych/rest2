package org.java.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.java.rest.repository.AccountRepository;
import org.java.rest.repository.dao.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ApplicationTest {

    private static final MediaType CONTENT_TYPE = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper();

        accountRepository.saveAll(Arrays.asList(
                new Account("100"), new Account("10"), new Account("0")
        ));
    }

    @Test
    public void accountNotFound() throws Exception {
        mockMvc.perform(post("/account/0/replenish")
                .content(amount("10"))
                .contentType(CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void notEnoughMoney() throws Exception {
        mockMvc.perform(post("/account/1/withdraw")
                .content(amount("1000"))
                .contentType(CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void replenishWithdraw() throws Exception {
        mockMvc.perform(post("/account/1/replenish")
                .content(amount("10"))
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk());

        mockMvc.perform(post("/account/1/withdraw")
                .content(amount("110"))
                .contentType(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    public void transfer() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<?>> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(executor.submit(() -> {
                mockMvc.perform(post("/account/2/transfer")
                        .content(transfer(3L, "0.1"))
                        .contentType(CONTENT_TYPE))
                        .andExpect(status().isOk());
                return null;
            }));
        }
        for (Future<?> future : list) {
            future.get();
        }

        mockMvc.perform(post("/account/2/transfer")
                .content(transfer(3L, "0.1"))
                .contentType(CONTENT_TYPE))
                .andExpect(status().isUnprocessableEntity());
    }

    private String amount(String amount) {
        ObjectNode node = objectMapper.createObjectNode();
        if (amount != null && !amount.isEmpty()) {
            node.put("amount", new BigDecimal(amount));
        }
        return node.toString();
    }

    private String transfer(Long account, String amount) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("account", account);
        node.put("amount", new BigDecimal(amount));
        return node.toString();
    }
}
