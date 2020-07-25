package cn.kj120.study.mybatis.handler;

import cn.kj120.study.mybatis.JsonTypeHandler;
import cn.kj120.study.mybatis.domain.Cate;

public class CateTypeHandler extends JsonTypeHandler<Cate> {
    public CateTypeHandler() {
        super(Cate.class);
    }
}
