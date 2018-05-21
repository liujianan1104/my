package com.dinfo.dispatcher.net;

import java.io.Serializable;

/**
 * @author yangxf
 */
public interface Response<T> extends Serializable {

    Boolean success();

    T data();

    String message();

}
