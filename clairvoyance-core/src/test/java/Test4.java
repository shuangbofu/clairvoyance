//import cn.shuangbofu.clairvoyance.core.utils.JSON;
//import com.alibaba.fastjson.annotation.JSONType;
//import com.alibaba.fastjson.parser.DefaultJSONParser;
//import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.google.common.collect.Lists;
//import lombok.AllArgsConstructor;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.lang.reflect.Type;
//import java.util.List;
//
///**
// * Created by shuangbofu on 2020/8/7 16:49
// */
//
//public class Test4 {
//
//    @Test
//    public void test() {
//
//        Dog dog = new Dog();
//        dog.dogName = "dog1001";
//
//        String text = JSON.toJSONString(dog, SerializerFeature.WriteClassName);
//        Assert.assertEquals("{\"@type\":\"dog\",\"dogName\":\"dog1001\"}", text);
//
//        Dog dog2 = (Dog) JSON.parseObject(text, Animal.class);
//
//        Assert.assertEquals(dog.dogName, dog2.dogName);
//
//
//    }
//
//    @Test
//    public void test2() {
//        Cat cat = new Cat();
//        cat.catName = "cat2001";
//
//        String text = JSON.toJSONString(cat, SerializerFeature.WriteClassName);
//        Assert.assertEquals("{\"@type\":\"cat\",\"catName\":\"cat2001\"}", text);
//
//        Cat cat2 = (Cat) JSON.parseObject(text, Animal.class);
//
//        Assert.assertEquals(cat.catName, cat2.catName);
//    }
//
//    @Test
//    public void test3() {
//        Dog dog = new Dog();
//        dog.dogName = "dog1001";
//        dog.animalType = AnimalType.dog;
//
//        Cat cat = new Cat();
//        cat.catName = "cat2001";
//        cat.animalType = AnimalType.cat;
//
//        List<Animal> animals = Lists.newArrayList(dog, cat);
//
//        String json = JSON.toJSONString(animals);
//        System.out.println(json);
//
//        List<Animal> parseAnimals = JSON.parseArray(json, Animal.class);
//
//        parseAnimals.forEach(animal -> {
////            if (animal instanceof Dog) {
////                Dog d = (Dog) animal;
////                d.printName();
////            } else if (animal instanceof Cat) {
////                Cat c = (Cat) animal;
////                c.printName();
////            }
//            animal.printName();
//        });
//    }
//
//    @AllArgsConstructor
//    enum AnimalType {
//
//        dog(Dog.class),
//        cat(Cat.class),
//        ;
//
//        private Class<? extends Animal> aClass;
//
//        public Animal getAnimal(Animal animal) {
//            return null;
//        }
//    }
//
//    @JSONType(seeAlso = {Dog.class, Cat.class}, deserializer = AnimalDeserializer.class)
//    public static class Animal {
//        public AnimalType animalType;
//
//        public void printName() {
//
//        }
//    }
//
//    @JSONType(typeName = "dog")
//    public static class Dog extends Animal {
//        public String dogName;
//
//        @Override
//        public void printName() {
//            System.out.println(dogName);
//        }
//    }
//
//    @JSONType(typeName = "cat")
//    public static class Cat extends Animal {
//        public String catName;
//
//        @Override
//        public void printName() {
//            System.out.println(catName);
//        }
//    }
//
//    class AnimalDeserializer implements ObjectDeserializer {
//
//        @Override
//        public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
//
//            System.out.println(o);
//            return null;
//        }
//
//        @Override
//        public int getFastMatchToken() {
//            return 0;
//        }
//    }
//
//}
