package quoters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ConfigurableListableBeanFactory factory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        String[] names = context.getBeanDefinitionNames();
        Arrays.stream(names).forEach(
                beanName -> {
//                    factory = ((ConfigurableApplicationContext) context).getBeanFactory();
                    BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
                    String originalClassName = beanDefinition.getBeanClassName();
                    try {
                        Class<?> originalClass = Class.forName(originalClassName);
                        Method[] methods = originalClass.getMethods();
                        Arrays.stream(methods).forEach(
                                method -> {
                                    if (method.isAnnotationPresent(PostProxy.class)) {
                                        Object bean = context.getBean(beanName);
                                        Method currentMethod = null;
                                        try {
                                            currentMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                                        } catch (NoSuchMethodException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            currentMethod.invoke(bean);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );
    }
}
