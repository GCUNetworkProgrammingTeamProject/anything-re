package com.anything.gradproject.entity;

import com.anything.gradproject.repository.MemberRepository;
import com.anything.gradproject.repository.ShoppingListRepository;
import com.anything.gradproject.service.ShoppingService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Entity
@Getter
@Setter
@ToString
@Table(name = "tb_shopping_list")
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingListSeq;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;


    @ManyToOne // 맞나..?
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lectures lectures;


    public static ShoppingList createShoppingList(Lectures lectures, Member member){

        ShoppingList shoppingList = new ShoppingList();

        shoppingList.setMember(member);
        shoppingList.setLectures(lectures);

        return shoppingList;
    }

}
