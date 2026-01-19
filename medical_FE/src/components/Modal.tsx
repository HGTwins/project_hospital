'use client';

import { Dispatch, SetStateAction, useEffect, useMemo, useState } from 'react';
import DetailContent from '@/components/DetailContent';
import Pagination from './Pagenation';
import { usePathname, useRouter, useSearchParams } from 'next/navigation';

interface ModalProps {
  title: string;
  isOpen: boolean;
  onClose: () => void;
  selectedHospId: number | null;
  setSelectedHospId: Dispatch<SetStateAction<number | null>>;
  setSelectedDeptCode: Dispatch<SetStateAction<string | null>>;
  data: any[];
  isLoading: boolean;
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number, sido?: string, sgg?: string, deptCode?: string) => void;
}

export default function Modal({ title, isOpen, onClose, selectedHospId, setSelectedDeptCode, setSelectedHospId, data, isLoading, currentPage, totalPages, onPageChange }: ModalProps) {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();

  const [selectedDept, setSelectedDept] = useState<string>('전체');
  
  const openModal = (id: number) => {
    // 현재 URL 파라미터를 유지하면서 hospId만 추가/변경
    const params = new URLSearchParams(searchParams.toString());
    params.set('hospId', id.toString());

    // 주소창을 /path?hospId=123 으로 변경 (페이지 새로고침 없이)
    router.push(`${pathname}?${params.toString()}`);
  }

  const deleteParams = () => {
    const params = new URLSearchParams(searchParams.toString());
    params.delete('hospId'); // 파라미터 삭제

    router.push(pathname);
  }

  // 부서명이 존재하는 데이터인지 확인 (필수의료병원 여부)
  const hasDeptInfo = useMemo(() => data.some(hosp => hosp.deptName), [data]);

  const handleDeptClick = (dept: string) => {
    setSelectedDept(dept);

    // 과목 이름에 따른 코드 매핑
    const deptMap: { [key: string]: string | undefined } = {
      '산부인과': '10',
      '소아청소년과': '11',
      '전체': undefined
    };

    const code = deptMap[dept];
    setSelectedDeptCode(code || null);
  };

  useEffect(() => {
    if (isOpen && totalPages === 1 && !selectedHospId && data?.[0]) {
      const singleId = data[0].hospitalId || data[0].hospital?.hospitalId;
      setSelectedHospId(singleId);
    }
  }, [isOpen, data, totalPages]);

   useEffect(() => {
    // 즉시 1페이지 데이터 요청
    // 부모로부터 받은 onPageChange(여기선 fetchCoreHospCount)를 호출
    onPageChange(0);
  }, [onPageChange])
  

  useEffect(() => {
    if (isOpen && data?.length === 1 && !selectedHospId) {
    const singleId = data[0].hospitalId || data[0].hospital?.hospitalId;
    if (singleId) {
      setSelectedHospId(singleId);
    }
  }
  }, [isOpen, data, selectedHospId]);

  // 진료과목 선택 초기화
  useEffect(() => {
    if (!isOpen) {
      setSelectedDept('전체');
      setSelectedDeptCode(null);
    }
  }, [isOpen]);
  

  if (!isOpen) return;

  return (
    <div className="fixed inset-0 z-9999">
      <div className="absolute inset-0 bg-black/50 flex justify-center items-center">
        <div className="relative bg-white p-6 rounded-xl w-1/2 h-8/10 shadow-2xl flex flex-col overflow-hidden">
          <div className="flex justify-between items-center mb-4">
            {selectedHospId && totalPages !== 1 ? 
              <>
                <button onClick={() => {setSelectedHospId(null); deleteParams();}} className='cursor-pointer'><img src='../arrow_left.svg'/></button>
                <button onClick={() => {onClose(); deleteParams();}} className="text-2xl cursor-pointer"><img src='../close.svg' /></button>
              </> : selectedHospId && totalPages === 1 ?
              <>
                <div></div>
                <button onClick={() => {onClose(); deleteParams();}} className="text-2xl cursor-pointer"><img src='../close.svg' /></button>
              </> :
              <>
                <h2 className="text-xl font-bold">{title}</h2>
                <button onClick={() => {onClose(); deleteParams();}} className="text-2xl cursor-pointer"><img src='../close.svg' /></button>
              </>
            }
          </div>

          {!selectedHospId && hasDeptInfo && (
            <div className="flex gap-2 mb-4 p-1 bg-gray-100 rounded-lg">
              {['전체', '소아청소년과', '산부인과'].map((dept) => (
                <button
                  key={dept}
                  onClick={() => handleDeptClick(dept)}
                  className={`flex-1 py-2 text-sm font-medium rounded-md transition-all ${selectedDept === dept
                    ? 'bg-white text-blue-600 shadow-sm'
                    : 'text-gray-500 hover:text-gray-700'
                    }`}
                >
                  {dept}
                </button>
              ))}
            </div>
          )}

          <div className="flex-1 overflow-y-auto p-3">
            {isLoading || (totalPages === 1 && !selectedHospId) ? 
              <div className=" h-full flex-1 flex flex-col justify-center items-center gap-4">
                <div className="w-12 h-12 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin"></div>
                <p className="text-gray-500 font-medium">데이터를 불러오는 중입니다...</p>
              </div>
              : selectedHospId ? <DetailContent hospitalId={selectedHospId} />
                : data && data.length > 0 ?
                  <ul className="space-y-3">
                    {data.map(hosp => (
                      <li key={hosp.hospitalId || hosp.hospital?.hospitalId} onClick={() => { setSelectedHospId(hosp.hospitalId || hosp.hospital?.hospitalId); openModal(hosp.hospitalId || hosp.hospital?.hospitalId); }}
                        className="p-4 border border-gray-300 rounded-lg shadow-sm hover:bg-gray-200 cursor-pointer transition">
                                                <div className="flex justify-between items-start">
                          <div className="font-bold text-lg">{hosp.hospital?.institutionName || hosp.institutionName}</div>
                          {hosp.deptName && (
                            <div className="flex flex-wrap gap-1 justify-end max-w-150px">
                              {hosp.deptName.split(',').map((dept: string) => (
                                <span
                                  key={dept.trim()}
                                  className={`text-[10px] px-1.5 py-0.5 rounded-md font-semibold ${
                                    // 현재 선택된 필터와 일치하면 강조색, 아니면 기본색
                                    selectedDept === dept.trim()
                                      ? 'bg-blue-600 text-white'
                                      : 'bg-blue-50 text-blue-600'
                                    }`}>
                                  {dept.trim()}
                                </span>
                              ))}
                            </div>
                          )}
                        </div>
                        <div className="text-sm text-gray-600 mt-1">📍 {hosp.hospital?.address || hosp.address}</div>
                        <div className="text-sm text-gray-600">📞 {hosp.hospital?.call || hosp.call}</div>
                      </li>
                    ))}
                  </ul> :
                  <div className="flex flex-col justify-center items-center">
                    <div className="text-center py-10 text-gray-500">
                      해당 조건의 병원 정보가 없습니다.
                    </div>
                  </div>
            }
          </div>

          <div>
            {!selectedHospId && !isLoading && <Pagination currentPage={currentPage} totalPages={totalPages} onPageChange={onPageChange} />}
          </div>
        </div>
      </div>
    </div>
  );
}