package newwater.com.newwater.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2018/5/11 0011.
 */


    @Table(name = "person")
    public class person {
        @Column(name = "ID", isId = true, autoGen = true)
        public int id;
        @Column(name = "NAME")
        public String name; // 姓名
        @Column(name = "AGE")
        public int age; // 年龄
        @Column(name = "SEX")
        public String sex; // 性别
        @Override
        public String toString() {
            return "person [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + "]";
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

