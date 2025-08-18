package com.itgroup;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;

import java.util.List;

// 메인 클래스 대신 실제 모든 업무를 총 잭임지는 매니저 클래스
public class MemberManager {
    private MemberDao dao = null; // 실제 데이터베이스와 연동하는 다오 클래스

    public MemberManager() {
        this.dao =new MemberDao();
    }

    public void selectAll() { // 모든 회원 조회
        List<Member> list = dao.selectAll();
        if (list.size() == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else{
            System.out.println(list); // toString 오버라이딩
        }
    }

    public void getSize(){ // 회원수 조회
        int cnt = dao.getSize();
        if (cnt == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else{
            System.out.println("회원은 총 " + cnt + "명입니다.");
        }
    }

    public void addOne() {
        System.out.println(dao.addOne());
    }

    public void deleteOne() {
        System.out.println(dao.deleteOne());
    }

    public void selectOne() {
        List<Member> list = dao.selectOne();
        if (list.size() == 0){
            System.out.println("회원이 존재하지 않습니다.");
        }else {
            System.out.println(list); // toString 오버라이딩
        }
    }
}
