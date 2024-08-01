package jpabook.jpashop.domain.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book   = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;
        //when
        Long OrderId = orderService.order(member.getId(),book.getId(),orderCount);

        //then
        Order getOrder = orderRepository.findOne(OrderId);

        assertEquals("상품 주문시 상태는 ORDERS", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount , getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 10, book.getStockQuantity());
    }

}