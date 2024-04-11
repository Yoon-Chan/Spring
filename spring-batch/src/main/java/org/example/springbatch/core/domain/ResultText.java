package org.example.springbatch.core.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Table(name = "result_text")
//entity의 일부 컬럼만 변경이 되었을 때, 그 값들에 대해서만 변경되는 쿼리가 실행되도록 업데이트 된다.
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class ResultText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String text;

}
