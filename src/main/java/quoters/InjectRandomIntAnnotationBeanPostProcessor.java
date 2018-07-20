package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

import static java.util.Objects.nonNull;

public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {

    // get invoked BEFORE init method
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);

            if (nonNull(annotation)) {
                int min = annotation.min();
                int max = annotation.max();
                Random random = new Random();
                int randomInt = min + random.nextInt(max - min);

                // Our FIELD is private so we need to set access to it
                field.setAccessible(true);

                // We need to inject out value to field, but for avoiding try catch exceptions, so we will use ReflectionUtils
                ReflectionUtils.setField(field, bean, randomInt);
            }
        });

        return bean;
    }

    // get invoked AFTER init method
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
