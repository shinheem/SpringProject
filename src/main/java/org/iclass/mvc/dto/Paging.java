package org.iclass.mvc.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Paging {
    private int currentPage;      //현재 페이지
    private int totalCount;         //글의 전체 갯수
    private int pageSize;         //한개페이지에 보여줄 글 갯수

    private int totalPage;         //전체 페이지 갯수

    private long startNo;         //화면에 보인는 글목록의 시작 rownum
    private long endNo;            //화면에 보인는 글목록의 마지막 rownum

    private int startPage;         //화면에 보이는 페이지목록의 시작페이지
    private int endPage;         //화면에 보이는 페이지목록의 마지막페이지   .

    public Paging(int currentPage, int totalCount, int pageSize) {   //외부(비지니스로직)에서 결정하고 전달되는값.
        this.totalCount=totalCount;
        this.pageSize=pageSize;

        totalPage = (int)Math.ceil((double)totalCount/pageSize); //ceil 은 올림입니다.
        //현재페이지 범위(1~totalPage) 의 유효성을 체크
        this.currentPage= (currentPage > totalPage)? totalPage:currentPage;
        this.currentPage= (currentPage < 1)? 1:this.currentPage;

        //이 부분이 제일 복잡합니다. 현재 페이지 currentPage 를 1,2,3,4,5 ... 등등으로 대입해서 계산해보세요.
        startNo=(this.currentPage-1)*pageSize+1;    //글목록 시작행번호(rownum)
        endNo = startNo + (pageSize-1);

        startPage = (this.currentPage-1)/10*10+1;       //페이지번호 리스트 10개씩

        endPage = startPage+9;
        endPage = endPage > totalPage ? totalPage:endPage;   //totalPage 보다 큰값에 대한 제한.
    }

}