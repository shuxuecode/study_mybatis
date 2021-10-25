package org.apache.ibatis.demo;

import java.util.Date;

/**
 * @date 2021/10/25
 */
public class Demo {

    private Integer id;

    private String name;

    private Long money;

    private Date birthday;

    //
    //

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
