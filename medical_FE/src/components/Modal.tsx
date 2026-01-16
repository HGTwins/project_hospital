'use client';

import { useEffect, useState } from 'react';
import DetailContent from '@/components/DetailContent';
import Pagination from './Pagenation';

interface ModalProps {
  title: string;
  isOpen: boolean;
  onClose: () => void;
  data: any[];
  isLoading: boolean;
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number, sido?:string, sgg?:string) => void;
}

export default function Modal({title, isOpen, onClose, data, isLoading, currentPage, totalPages, onPageChange}: ModalProps) {
  const [selectedHospId, setSelectedHospId] = useState<number | null>(null);

  useEffect(() => {
    if (isOpen && data.length === 1 && !selectedHospId) {
      const singleId = data[0].hospitalId || data[0].hospital?.hospitalId;
      setSelectedHospId(singleId);
    }
  }, [isOpen, data]);

  if (!isOpen) return;

  const handleClose = () => {
    setSelectedHospId(null);
    onClose();
  }

  return (
    <div className="fixed inset-0 z-9999">
      <div className="absolute inset-0 bg-black/50 flex justify-center items-center">
        <div className="relative bg-white p-6 rounded-xl w-1/2 h-8/10 shadow-2xl flex flex-col overflow-hidden">
          <div className="flex justify-between items-center mb-4">
            {data.length !== 1 && selectedHospId ?
              <>
                <button onClick={() => setSelectedHospId(null)} className='cursor-pointer'><img src='../arrow_left.svg'/></button>
                <button onClick={handleClose} className="text-2xl cursor-pointer"><img src='../close.svg' /></button>
              </> : 
              <>
                <h2 className="text-xl font-bold">{title}</h2>
                <button onClick={handleClose} className="text-2xl cursor-pointer"><img src='../close.svg' /></button>
              </>
            }
          </div>

          <div className="flex-1 overflow-y-auto p-3">
            {isLoading ? 
              <div className=" h-full flex-1 flex flex-col justify-center items-center gap-4">
                <div className="w-12 h-12 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin"></div>
                <p className="text-gray-500 font-medium">데이터를 불러오는 중입니다...</p>
              </div>
              : selectedHospId ? <DetailContent hospitalId={selectedHospId} /> 
              : data && data.length > 0 ?
              <ul className="space-y-3">
                {data.map(hosp => (
                  <li key={hosp.hospitalId || hosp.hospital?.hospitalId} onClick={() => setSelectedHospId(hosp.hospitalId || hosp.hospital?.hospitalId)}
                      className="p-4 border border-gray-300 rounded-lg shadow-sm hover:bg-gray-200 cursor-pointer transition">
                    <div className="font-bold text-lg">{hosp.hospital?.institutionName || hosp.institutionName}</div>
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
            {!selectedHospId && <Pagination currentPage={currentPage} totalPages={totalPages} onPageChange={onPageChange} />}
          </div>
        </div>
      </div>
    </div>
  );
}