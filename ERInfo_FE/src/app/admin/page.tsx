'use client'

import { useRouter } from "next/navigation";
import { useEffect, useState, useCallback } from "react";

export default function AdminPage() {
    const [viewType, setViewType] = useState<'member' | 'review'>('member');
    const [members, setMembers] = useState<any[]>([]);
    const [allReviews, setAllReviews] = useState<any[]>([]);
    const [selectedMember, setSelectedMember] = useState<string | null>(null);
    const [memberReviews, setMemberReviews] = useState<any[]>([]);

    const router = useRouter();

    // 공통 헤더 및 토큰 가져오기 함수
    const getHeaders = useCallback(() => {
        const token = sessionStorage.getItem('jwtToken');
        if (!token) return null;
        return {
            'Authorization': token.startsWith('Bearer ') ? token : `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
    }, []);

    // 1. 멤버 목록 조회
    const fetchMembers = async () => {
        const headers = getHeaders();
        if (!headers) return;

        try {
            const resp = await fetch(`http://10.125.121.178:8080/api/admin/getMembers`, { headers });
            if (resp.ok) {
                setMembers(await resp.json());
            } else {
                handleError(resp.status);
            }
        } catch (err) { console.error("멤버 조회 실패:", err); }
    };

    // 2. 전체 리뷰 조회
    const fetchAllReviews = async () => {
        const headers = getHeaders();
        if (!headers) return;

        try {
            const resp = await fetch(`http://10.125.121.178:8080/api/review`, { headers });
            if (resp.ok) {
                const data = await resp.json();
                setAllReviews(data.content || data);
            } else {
                handleError(resp.status);
            }
        } catch (err) { console.error("전체 리뷰 조회 실패:", err); }
    };

    // 3. 특정 멤버 리뷰 조회 (팝업용)
    const fetchMemberReviews = async (username: string) => {
        const headers = getHeaders();
        if (!headers) return;

        try {
            const resp = await fetch(`http://10.125.121.178:8080/api/review/memberId/${username}`, { headers });
            if (resp.ok) {
                const data = await resp.json()
                setMemberReviews(data.content);
                setSelectedMember(username);
            } else {
                handleError(resp.status);
            }
        } catch (err) { console.error("멤버별 리뷰 조회 실패:", err); }
    };

    // 4. 삭제 로직 (멤버/리뷰 공통 헤더 적용)
    const deleteMember = async (username: string) => {
        if (!confirm(`${username} 회원을 삭제하시겠습니까?`)) return;
        const headers = getHeaders();
        if (!headers) return;

        const resp = await fetch(`http://10.125.121.178:8080/api/admin/getMember/${username}`, { 
            method: 'DELETE',
            headers 
        });
        if (resp.ok) { alert("삭제되었습니다."); fetchMembers(); }
    };

    const deleteReview = async (seq: number) => {
        if (!confirm("이 리뷰를 삭제하시겠습니까?")) return;
        const headers = getHeaders();
        if (!headers) return;

        const resp = await fetch(`http://10.125.121.178:8080/api/review/${seq}`, { 
            method: 'DELETE',
            headers 
        });
        if (resp.ok) {
            alert("리뷰가 삭제되었습니다.");
            if (selectedMember) fetchMemberReviews(selectedMember);
            if (viewType === 'review') fetchAllReviews();
        }
    };

    // 에러 공통 처리
    const handleError = (status: number) => {
        if (status === 401 || status === 403) {
            alert("권한이 없거나 세션이 만료되었습니다. 다시 로그인해주세요.");
            router.push("/")
        } else {
            alert("데이터를 불러오는 중 오류가 발생했습니다.");
        }
    };

    useEffect(() => {
        const role = sessionStorage.getItem('role');
        if (role !== 'ROLE_ADMIN' || role == null) {
            alert("관리자 권한이 없습니다.");
            router.back();
            return;
        }
        fetchMembers();
    }, []);

    return (
        <div className="p-8 max-w-6xl mx-auto">
            <h1 className="text-3xl font-bold mb-6 text-gray-800">통합 관리자 센터</h1>

            {/* 탭 메뉴 */}
            <div className="flex gap-4 mb-6 border-b">
                <button 
                    onClick={() => { setViewType('member'); fetchMembers(); }}
                    className={`pb-2 px-4 font-bold transition-all ${viewType === 'member' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-400'}`}
                >
                    멤버 관리
                </button>
                <button 
                    onClick={() => { setViewType('review'); fetchAllReviews(); }}
                    className={`pb-2 px-4 font-bold transition-all ${viewType === 'review' ? 'border-b-2 border-blue-600 text-blue-600' : 'text-gray-400'}`}
                >
                    전체 리뷰 관리
                </button>
            </div>

            {/* 내용 테이블 영역 (생략 없이 로직 동일) */}
            {viewType === 'member' ? (
                <div className="bg-white rounded-xl shadow border">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-gray-50 border-b">
                                <th className="p-4 font-semibold text-gray-600">아이디</th>
                                <th className="p-4 font-semibold text-gray-600">닉네임</th>
                                <th className="p-4 font-semibold text-gray-600">권한</th>
                                <th className="p-4 font-semibold text-gray-600">액션</th>
                            </tr>
                        </thead>
                        <tbody>
                            {members.map((m, i) => (
                                <tr key={i} className="border-b hover:bg-gray-50 transition-colors">
                                    <td className="p-4 text-sm">{m.username}</td>
                                    <td className="p-4 text-sm">{m.alias}</td>
                                    <td className="p-4"><span className="px-2 py-1 bg-blue-50 text-blue-600 rounded text-xs font-bold">{m.role}</span></td>
                                    <td className="p-4 flex gap-2">
                                        <button onClick={() => fetchMemberReviews(m.username)} className="px-3 py-1 bg-white border border-gray-300 rounded text-xs hover:bg-gray-50">리뷰 확인</button>
                                        <button onClick={() => deleteMember(m.username)} className="px-3 py-1 bg-red-500 text-white rounded text-xs hover:bg-red-600">삭제</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div className="bg-white rounded-xl shadow border">
                    <table className="w-full text-left">
                        <thead>
                            <tr className="bg-gray-50 border-b">
                                <th className="p-4 font-semibold text-gray-600">작성자</th>
                                <th className="p-4 font-semibold text-gray-600">닉네임</th>
                                <th className="p-4 font-semibold text-gray-600">내용</th>
                                <th className="p-4 font-semibold text-gray-600 text-center">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            {allReviews.map((r) => (
                                <tr key={r.seq} className="border-b hover:bg-gray-50">
                                    <td className="p-4 text-sm">{r.username}</td>
                                    <td className="p-4 text-sm">{r.alias}</td>
                                    <td className="p-4 text-sm text-gray-600 leading-relaxed">{r.content}</td>
                                    <td className="p-4 text-center">
                                        <button onClick={() => deleteReview(r.seq)} className="text-red-500 hover:text-red-700 font-bold text-xs uppercase underline-offset-4 hover:underline">삭제</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {/* 멤버 리뷰 팝업 모달 */}
            {selectedMember && (
                <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-9999 backdrop-blur-sm p-4">
                    <div className="bg-white w-full max-w-2xl rounded-2xl shadow-2xl overflow-hidden animate-in fade-in zoom-in duration-200">
                        <div className="flex justify-between items-center p-5 border-b bg-gray-50">
                            <h2 className="text-xl font-bold text-gray-800"><span className="text-blue-600">{selectedMember}</span> 님의 리뷰</h2>
                            <button onClick={() => setSelectedMember(null)} className="p-2 hover:bg-gray-200 rounded-full transition-colors text-gray-500 text-xl">✕</button>
                        </div>
                        <div className="p-5 max-h-[60vh] overflow-y-auto space-y-3">
                            {memberReviews.length > 0 ? memberReviews.map((r) => (
                                <div key={r.seq} className="p-4 bg-gray-50 border border-gray-100 rounded-xl flex justify-between items-start gap-4 hover:border-blue-200 transition-colors">
                                    <p className="text-sm text-gray-700 flex-1">{r.content}</p>
                                    <button onClick={() => deleteReview(r.seq)} className="shrink-0 text-xs text-red-500 font-bold border border-red-200 px-2 py-1 rounded hover:bg-red-50">삭제</button>
                                </div>
                            )) : <div className="text-center py-12 text-gray-400">작성한 리뷰가 없습니다.</div>}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}