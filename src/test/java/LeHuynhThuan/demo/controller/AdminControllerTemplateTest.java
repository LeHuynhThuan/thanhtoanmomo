package LeHuynhThuan.demo.controller;

import LeHuynhThuan.demo.entity.Category;
import LeHuynhThuan.demo.service.BookService;
import LeHuynhThuan.demo.service.CartService;
import LeHuynhThuan.demo.service.CategoryService;
import LeHuynhThuan.demo.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminController.class)
class AdminControllerTemplateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private CartService cartService;

    @MockBean
    private MongoTemplate mongoTemplate;

    @Test
    @WithMockUser(roles = "ADMIN")
    void categoriesPageRenders() throws Exception {
        Category c = new Category();
        c.setId("1");
        c.setName("Test Category");
        when(categoryService.getAllCategories()).thenReturn(List.of(c));

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/categories"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quản Lý Thể Loại")));
    }
}

