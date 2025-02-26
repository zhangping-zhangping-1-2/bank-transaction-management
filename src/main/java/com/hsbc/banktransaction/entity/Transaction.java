package com.hsbc.banktransaction.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(indexes = {@Index(name = "idx_code", columnList = "code", unique = true)
        , @Index(name = "idx_type", columnList = "transactionType")})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "交易类型不能为空")
    @Length(message = "type长度必须大于2小于20", min = 2, max = 20)
    private String transactionType;

    @Column(name = "from_account", length = 20)
    @NotBlank(message = "转出账号不能为空")
    private String fromAccount;

    @Column(name = "to_account", length = 20)
    @NotBlank(message = "转入账号不能为空")
    private String toAccount;

    @Column(precision=10, scale=2)
    @NotNull(message = "交易金额不能为空")
    @DecimalMin(value = "0.01", message = "交易金额必须大于等于 0.01")
    private BigDecimal amount;

    @Column(name = "create_time", updatable = false)
    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    //@NotBlank(message = "交易描述不能为空")
    @Length(message = "type长度必须大于2小于50", min = 2, max = 50)
    private String description;

    @NotBlank(message = "交易码不能为空")
    private String code;

    // 构造函数、Getter和Setter方法
    public Transaction() {
    }

    public Transaction(String transactionType, String fromAccount, String toAccount, BigDecimal amount, String status) {
        this.transactionType = transactionType;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        this.createTime  = LocalDateTime.now();
        this.updateTime  = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime  = LocalDateTime.now();
    }


}