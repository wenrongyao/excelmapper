# excel通用mapper,可以将实体对象存储到Excel表格中，也可以从excel表格中读取数据自动转为实体对象
#### 获取方式
此代码没有上传maven中央仓库，读者需要手动clone下此代码，通过mvn install命令将工程安装到本地仓库，然后通过下面依赖引入到项目中
```xml
  <dependency>
    <groupId>com.ecm</groupId>
    <artifactId>excelmapper</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>
```

#### 1）@Head(value = "姓名", orderBy = 0) 指定对象的属性和excel表头的对应关系，value指定表头名称，orderBy指定排序
#### 2）@Transparent 指定不需要映射的字段
#### 3）映射的对象一定要写get，set方法
#### 一个例子
```java
  public class User {
    @Head(value = "姓名", orderBy = 0)
    private String name;
    @Head(value = "年龄", orderBy = 1)
    private Integer age;
    @Head(value = "地址", orderBy = 2)
    private String address;
    @Head(value = "出生年月", orderBy = 3)
    private Date birthDate;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}

```
#### 测试代码
```java 
public class ExcelMapperTest {
    @Test
    public void readAll() throws Exception {
        ExcelMapper<User> userExcelMapper = new ExcelMapper<>("F:/Test.xlsx");
        List<User> users = userExcelMapper.readAll(User.class);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void insert() throws Exception {
        ExcelMapper<User> userExcelMapper2 = new ExcelMapper<>("F:/Test.xlsx");
        User user = new User();
        user.setName("李四");
        user.setAge(25);
        user.setAddress("广东省广州市");
        user.setBirthDate(new Date());
        System.out.println(userExcelMapper2.insert(user));
    }
```
