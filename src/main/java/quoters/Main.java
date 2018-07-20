package quoters;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;

public class Main {
    static final Logger log = Logger.getLogger("Main");

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        log.info("context: " + context.getBeansOfType(TerminatorQuoter.class));
        log.info("context: " + context.getBeansOfType(InjectRandomIntAnnotationBeanPostProcessor.class));
        log.info("context: " + context.getBeansOfType(SimpleBean.class));
        context.getBean(TerminatorQuoter.class).sayQuote();
    }
}
