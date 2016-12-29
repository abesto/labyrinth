package net.abesto.labyrinth.fsm;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(InStates.class)
public @interface InState {
    Class<? extends States.State> value();
}
