package net.livefootball.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PageableUtil {

    public int getLength(int count,int size){
        if(count < size){
            return 1;
        }else if(count % size != 0){
            return count/size + 1;
        }else {
            return count/size;
        }
    }

    public Pageable getCheckedPageable(Pageable currentPageable,int length){
        if(currentPageable.getPageNumber() >= length){
            return PageRequest.of(0,currentPageable.getPageSize());
        }else {
            return currentPageable;
        }
    }
}