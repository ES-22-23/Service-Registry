package pt.ua.deti.es.serviceregistry.data.dto;

import java.io.Serializable;

public interface DataTransferObject<T> extends Serializable {

    T toModel();

}
