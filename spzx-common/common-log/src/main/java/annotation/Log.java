package annotation;

import enums.OperatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    // 模块名称
    public String title() ;
    // 操作人类别
    public OperatorType operatorType() default OperatorType.MANAGE;
    // 业务类型（0其它 1新增 2修改 3删除）
    public int businessType() ;
    // 是否保存请求的参数
    public boolean isSaveRequestData() default true;
    // 是否保存响应的参数
    public boolean isSaveResponseData() default true;
}
