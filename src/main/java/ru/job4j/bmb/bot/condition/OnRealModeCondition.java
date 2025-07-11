package ru.job4j.bmb.bot.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnRealModeCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !"fake".equalsIgnoreCase(context.getEnvironment().getProperty("telegram.mode", "real"));
    }
}
