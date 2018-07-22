package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Arrays;

import static java.util.Objects.nonNull;

public class DeprecationHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] names = configurableListableBeanFactory.getBeanDefinitionNames();
        Arrays.stream(names).forEach(
                name -> {
                    BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(name);
                    String beanClassName = beanDefinition.getBeanClassName();
                    try {
                        Class<?> beanClass = Class.forName(beanClassName);
                        DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);
                        if (nonNull(annotation))
                            beanDefinition.setBeanClassName(annotation.newImpl().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );
    }
}
