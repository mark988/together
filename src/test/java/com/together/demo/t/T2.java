package com.together.demo.t;

import javax.swing.text.html.Option;
import java.lang.reflect.Field;
import java.net.SocketOption;
import java.util.Optional;
import java.util.function.Supplier;

public class T2 {
    public static void main(String[] args) {
        Student student= new Student();
        Course course=new Course();
       // course.setCname("aaaa");
        student.setCourse(course);
        if(student!=null){
            if(student.getCourse()!=null) {
                System.out.println(student.getCourse().getCname());
            }else {
                System.out.println("nothing");
            }
        }else{
            System.out.println(" student is null");
        }

        /**
         * 2 thoed
         */

       // Optional.of(new Student()).map(Course::getCname.ifPresent(System.out::println);
        boolean ok = resolve(()->student.getCourse().getCname()).isEmpty();
        if(!ok){
           String x = resolve(()->student.getCourse().getCname()).get();
           System.out.println(x);
        }else{
            System.out.println(" is null");
        }
       // System.out.println(ok);
      //  resolve(()->student.getCourse().getCname()).ifPresent(System.out::println);
        //  System.out.println(
      //  Optional.of(new Student()).map(Course::getCname.ifPresent(System.out::println); //    .ifPresent(System.out::println);

      Boolean  n =   isObjectFieldEmpty(student);
        System.out.println(n);

    }

    public static <T>  Optional<T> resolve(Supplier<T> resolver){
        try{
            T result =resolver.get();
            return Optional.ofNullable(result);
        }catch(NullPointerException   e){
            return Optional.empty();
        }
    }


    public static boolean isObjectFieldEmpty(Object object) {
        boolean flag = false;
        if (object != null) {
            Class<?> entity = object.getClass();
            Field[] fields = entity.getDeclaredFields();//获取该类的所有成员变量（私有的）
            for (Field field : fields) {
                System.out.println(field);
                try {
                    field.setAccessible(true);
                    if (field.get(object) != null && !"".equals(field.get(object))) {
                        flag = true;
                        break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

}
