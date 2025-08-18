package com.itgroup;

import com.itgroup.dao.MemberDao;

// 메인 클래스 대신 실제 모든 업무를 총 잭임지는 매니저 클래스
public class MemberManager {
    private MemberDao dao = null; // 실제 데이터베이스와 연동하는 다오 클래스

    public MemberManager() {
        this.dao =new MemberDao();
    }

    public void selectAll(){ // 모든 멤버 조회

    }

    public void getSize(){ // 멤버수 조회

    }
}
