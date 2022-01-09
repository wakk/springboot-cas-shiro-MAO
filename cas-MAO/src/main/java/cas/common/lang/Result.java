package cas.common.lang;


import lombok.Data;

@Data
public class Result {
    private int code;
    private String msg;
    private Object object;

    public static Result succ(Object object) {
        return succ(200, "操作成功", object);
    }

    public static Result fail(String msg, Object object) {
        return succ(400, msg, object);
    }

    public static Result fail(String msg) {
        return succ(400, msg, null);
    }

    public static Result succ(int code, String msg, Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setObject(object);
        return result;
    }

    public static Result fail(int code, String msg, Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setObject(object);
        return result;
    }
}
