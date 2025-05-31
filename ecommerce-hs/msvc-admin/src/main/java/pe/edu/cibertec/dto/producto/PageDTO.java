package pe.edu.cibertec.dto.producto;

import java.util.List;

public class PageDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean last;
}
