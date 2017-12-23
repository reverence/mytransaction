package com.my.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tufei on 2017/12/20.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
    /**
     * int TRANSACTION_NONE             = 0;
     int TRANSACTION_READ_UNCOMMITTED = 1;
     int TRANSACTION_READ_COMMITTED   = 2;
     int TRANSACTION_REPEATABLE_READ  = 4;
     int TRANSACTION_SERIALIZABLE     = 8;
     * @return
     */
    int isolation() default 2;//READ_COMMITED
    String propagation() default "REQUIRED";
}
