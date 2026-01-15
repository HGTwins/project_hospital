'use client'
import { useEffect, useState } from 'react';
import KakaoMap from '@/components/KakaoMap';
import SideBar from '@/components/SideBar';
import Header from '@/components/Header';
import ScoreCard from '@/components/ScoreCard';
import SelectBox from '@/components/SelectBox';
import Modal from '@/components/Modal';
import dynamic from 'next/dynamic';
const Dashboard = dynamic(() => import('@/components/Dashboard'), { ssr: false });
import { HospCategory } from '@/types/HospCategory';
import { HospDept } from '@/types/HospDept';
import { HospLocation } from '@/types/HospLocation';
import { HospInfo } from '@/types/HospInfo';

export default function medicalInfoPage() {
  const [collapsed, setCollapsed] = useState<boolean>(false); // 사이드바 토글

  // 스코어 카드관련 변수
  const [totalCount, setTotalCount] = useState<number>(0); // 전체 병원 수
  const [nightHosp, setNightHosp] = useState<number>(0); // 야간진료 운영 병원 수
  const [holidayHosp, setHolidayHosp] = useState<number>(0); // 공휴일 운영 병원 수
  const [coreHosp, setCoreHosp] = useState<number>(0); // 필수의료 운영 병원 수

  // 모달 창 관련 변수
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false); // 닫힘 버튼
  const [modalData, setModalData] = useState<HospInfo[] | HospLocation[]>([]); // 모달 데이터
  const [modalTitle, setModalTitle] = useState<string>(''); // 스코어 카드별 모달 제목
  const [isLoading, setIsLoading] = useState<boolean>(false); // 로딩 유무

  // 지도 관련 변수
  const [sidoList, setSidoList] = useState<string[]>([]); // 시도 목록
  const [sggList, setSggList] = useState<string[]>([]); // 시군구 목록
  const [selectedSido, setSelectedSido] = useState<string>(''); // 선택된 시도
  const [selectedSgg, setSelectedSgg] = useState<string>(''); // 선택된 시군구
  const [markers, setMarkers] = useState<HospLocation[]>([]); // 전체 병원 마커 보관
  const [displayMarker, setDisplayMarker] = useState<HospLocation[]>([]); // 줌 화면에 따라 보이는 마커

  // 차트 관련 변수
  const [hospCate, setHospCate] = useState<HospCategory[]>([]); // 병원 유형
  const [hospDept, setHospDept] = useState<HospDept[]>([]); // 진료 과목
  const categoryData = { // 병원 유형 정보(도넛차트)
    series: hospCate.map(item => item.count),
    labels: hospCate.map(item => item.typeName)
  }
  const deptData = { // 진료 과목 정보(바 차트)
    series: [{name: '병원 수', data: hospDept.map(item => item.count)}],
    labels: hospDept.map(item => item.deptCode)
  }

  // 페이징
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [pageChange, setPageChange] = useState<(page?: number, sido?: string, sgg?: string) => Promise<void>>(() => async () => {});

  // 병원 수 불러오기
  const fetchHospCount = async(sido?: string, sgg?: string) => {
    let url = 'http://10.125.121.178:8080/api/medicalCountHospital';
    if(sido && sgg) {
      url += `?sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `?sidoName=${encodeURIComponent(sido)}`
    }

    try{
      const resp = await fetch(url);
      if(!resp.ok) {
        throw new Error("병원 수를 불러오는데 실패했습니다!");
      }
      const data = await resp.json();
      setTotalCount(data);
    } catch(error) {
      console.error(error);
    }
  }

  // 야간진료 운영 병원 수 불러오기
  const fetchNightHospCount = async(page?: number, sido?: string, sgg?: string) => {
    let url = `http://10.125.121.178:8080/api/medicalNight?page=${page}&size=5`;
    if(sido && sgg) {
      url += `&sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `&sidoName=${encodeURIComponent(sido)}`
    }

    try{
      const resp = await fetch(url);
      if(!resp.ok) {
        throw new Error("야간진료 운영 병원 정보를 불러오는데 실패했습니다!");
      }
      const data = await resp.json();
      setNightHosp(data.totalElements);
      setModalData(data.content || []);
      setTotalPages(data.totalPages || 0);
      setCurrentPage(page!);
    } catch(error) {
      console.error(error);
    }
  }

  // 공휴일 운영 병원 수 불러오기
  const fetchHolidayHospCount = async(page?: number, sido?: string, sgg?: string) => {
    let url = `http://10.125.121.178:8080/api/medicalHoliday?page=${page}&size=5`;
    if(sido && sgg) {
      url += `&sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `&sidoName=${encodeURIComponent(sido)}`
    }

    try{
      const resp = await fetch(url);
      if(!resp.ok) {
        throw new Error("공휴일 운영 병원 정보를 불러오는데 실패했습니다!");
      }
      const data = await resp.json();
      setHolidayHosp(data.totalElements);
      setModalData(data.content || []);
      setTotalPages(data.totalPages || 0);
      setCurrentPage(page!);
    } catch(error) {
      console.error(error);
    }
  }

  // 필수의료 운영 병원 수 불러오기
  const fetchCoreHospCount = async(page?: number, sido?: string, sgg?: string) => {
    let url = `http://10.125.121.178:8080/api/medicalEssential?page=${page}&size=5`;
    if(sido && sgg) {
      url += `&sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `&sidoName=${encodeURIComponent(sido)}`
    }

    try{
      const resp = await fetch(url);
      if(!resp.ok) {
        throw new Error("필수의료 운영 병원 정보를 불러오는데 실패했습니다!");
      }
      const data = await resp.json();
      setCoreHosp(data.totalElements);
      setModalData(data.content || []);
      setTotalPages(data.totalPages || 0);
      setCurrentPage(page!);
    } catch(error) {
      console.error(error);
    }
  }

  // select 박스의 시도 목록 불러오기
  const fetchSidoList = async() => {
    try{
      const resp = await fetch('http://10.125.121.178:8080/api/sidoName');
      if(!resp.ok) {
        throw new Error('시도 정보를 불러오는데 실패했습니다!');
      }
      const sido = await resp.json();
      setSidoList(sido);
    } catch(error) {
      console.log(error);
    }
  }
  
  // select 박스의 시군구 목록 불러오기
  const fetchSggList = async(sido: string) => {
    try{
      const resp = await fetch(`http://10.125.121.178:8080/api/sigunguName?sidoName=${encodeURIComponent(sido)}`);
      if(!resp.ok) {
        throw new Error('시군구 정보를 불러오는데 실패했습니다!');
      }
      const sgg = await resp.json();
      setSggList(sgg);
    } catch(error) {
      console.log(error);
    }
  }

  // 병원 유형 불러오기
  const fetchHospCategory = async(sido?: string, sgg?: string) => {
    let url = 'http://10.125.121.178:8080/api/medicalType';
    if(sido && sgg) {
      url += `?sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `?sidoName=${encodeURIComponent(sido)}`
    }

    try{
      const resp = await fetch(url);
      if(!resp.ok) {
        throw new Error('병원 카테고리 정보를 불러오는데 실패했습니다!');
      }
      const category = await resp.json();
      setHospCate(category);
    } catch(error) {
      console.log(error);
    }
  }

  // 병원 진료과목 불러오기
  const fetchHospDept = async(sido?: string, sgg?: string) => {
    let url = 'http://10.125.121.178:8080/api/medicalDept?topN=5';
    if(sido && sgg) {
      url += `&sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `&sidoName=${encodeURIComponent(sido)}`
    }

    try{
      const resp = await fetch(url);
      if(!resp.ok) {
        throw new Error('병원 부서? 정보를 불러오는데 실패했습니다!');
      }
      const dept = await resp.json();
      setHospDept(dept);
    } catch(error) {
      console.log(error);
    }
  }

  // 병원 위치정보 불러오기(마커, 커스텀 오버레이)
  const fetchHospLocation = async(level?: number) => {
    try{
      const resp = await fetch(`http://10.125.121.178:8080/api/medicalLocation?${level}`);
      if(!resp.ok) {
        throw new Error("병원 위치 정보를 불러오는데 실패했습니다!");
      }
      const data = await resp.json();
      setMarkers(data);
      setDisplayMarker(data);
    } catch(error) {
      console.error(error);
    }
  }

  // 병원 간단정보 불러오기(마커(시군구 선택 시), 모달 창)
  const fetchHospInfo = async (page?: number, sido?: string, sgg?: string) => {
    let url = `http://10.125.121.178:8080/api/medicalInfo?page=${page}&size=5`
    if(sido && sgg) {
      url += `&sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `&sidoName=${encodeURIComponent(sido)}`;
    }
    setIsLoading(true);
    try {
      const resp = await fetch(url);

      if(!resp.ok) {
        throw new Error("병원 정보를 불러오는데 실패했습니다!");
      }

      const data = await resp.json();
      setModalData(data.content || []);
      setDisplayMarker(data.content || []);
      setTotalPages(data.totalPages || 0);
      setCurrentPage(page!);
    } catch (e) {
      console.error("데이터 로드 실패:", e);
    } finally {
      setIsLoading(false);
    }
  };

    const fetchHospInfo2 = async (sido?: string, sgg?: string) => {
    let url = `http://10.125.121.178:8080/api/medicalInfo?size=7000`
    if(sido && sgg) {
      url += `&sidoName=${encodeURIComponent(sido)}&sigunguName=${encodeURIComponent(sgg)}`;
    } else if(sido) {
      url += `&sidoName=${encodeURIComponent(sido)}`;
    }
    setIsLoading(true);
    try {
      const resp = await fetch(url);

      if(!resp.ok) {
        throw new Error("병원 정보를 불러오는데 실패했습니다!");
      }

      const data = await resp.json();
      setDisplayMarker(data.content || []);
    } catch (e) {
      console.error("데이터 로드 실패:", e);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchSidoList(); // 시도 목록 나타내기
    fetchHospLocation(); 
  }, []);

  useEffect(() => {
    if (!selectedSido) return; 
    fetchSggList(selectedSido); // 시도가 선택되면 시군구 리스트 나타내기
  }, [selectedSido]);

  useEffect(() => {
    fetchHospCategory(selectedSido, selectedSgg);
    fetchHospDept(selectedSido, selectedSgg);
    fetchHospCount(selectedSido, selectedSgg);
    fetchNightHospCount(0, selectedSido, selectedSgg);
    fetchHolidayHospCount(0, selectedSido, selectedSgg);
    fetchCoreHospCount(0, selectedSido, selectedSgg);
    fetchHospInfo(0, selectedSido, selectedSgg);
    fetchHospInfo2(selectedSido, selectedSgg);
  }, [selectedSido, selectedSgg]);

  // 선택한 시도를 바꿨을 때 처리
  const handleSidoChange = (value: string) => {
    setSelectedSido(value); // 선택한 시도를 value값으로 변경
    setSelectedSgg(''); // 선택된 시군구 초기화
    setSggList([]); // 이전 시군구 리스트 제거
  }

  // 지도가 움직일 때 호출
  const handleBoundsChange = (swLat: number, neLat: number, swLng: number, neLng: number) => {
    if (selectedSido) return;

    const filtered = markers.filter(m => m.latitude >= swLat && m.latitude <= neLat && m.longitude >= swLng && m.longitude <= neLng);
    // setDisplayMarker(filtered);

    if (JSON.stringify(displayMarker) !== JSON.stringify(filtered)) {
      setDisplayMarker(filtered);
    }
  }

  const handleModalData = async(type: string) => {
    setModalData([]);
    setIsModalOpen(true);
    setIsLoading(true);
    setCurrentPage(0);

    try {
      switch (type) {
        case 'total':
          setModalTitle("🏥 전체 병원 수");
          await fetchHospInfo(0, selectedSido, selectedSgg);
          setPageChange(() => fetchHospInfo)   
          break;
        case 'night':
          setModalTitle("🌜 야간진료 운영 병원");
          await fetchNightHospCount(0, selectedSido, selectedSgg);
          setPageChange(() => fetchNightHospCount)
          break;
        case 'holiday':
          setModalTitle("🗓️ 일요일/공휴일 진료 병원");
          await fetchHolidayHospCount(0, selectedSido, selectedSgg);
          setPageChange(() => fetchHolidayHospCount)
          break;
        case 'core':
          setModalTitle("🚨 필수의료 운영 병원");
          await fetchCoreHospCount(0, selectedSido, selectedSgg);
          setPageChange(() => fetchCoreHospCount)
          break;
      }
    } finally {
      setIsLoading(false); // 성공하든 실패하든 로딩 종료
    }
  }

  const handleDetailView = async (hospitalId: number) => {
    setIsLoading(true);
    setIsModalOpen(true); 
    setModalData([]);

  try {
    const resp = await fetch(`http://10.125.121.178:8080/api/medicalId?hospitalId=${hospitalId}`);
    if (!resp.ok) throw new Error("상세 정보 호출 실패");
    const data = await resp.json();
    
    setModalData([data]); 
    setTotalPages(0);
    setPageChange(() => async () => {}); 
  } catch (e) {
      console.error("상세 정보 로드 실패:", e);
    }finally {
      setIsLoading(false);
    }
  }

  const handleInit = () => {
    setSelectedSido('');
    setSelectedSgg('');
    setSggList([]);
    fetchHospLocation();
  }


  return (
    <div className="flex min-h-screen xl:h-screen overflow-hidden">
      <SideBar collapsed={collapsed} setCollapsed={setCollapsed} />
      <div className={`${collapsed ? 'md:pl-16' : 'md:pl-55'} bg-gray-100 relative flex flex-1 mt-14`}> 
        <main className='flex flex-1 flex-col overflow-hidden'>
          <Header />
          <div className='p-5 flex-1 min-h-0 grid grid-cols-12 grid-rows-[auto_1fr] gap-4'>
              <div className='xl:col-span-8 grid grid-cols-4 gap-4 col-span-12'>
                <ScoreCard title="전체 병원 수" content={totalCount} onOpen={() => handleModalData('total')}
                           color="blue" imgSrc='hospital'/>
                <ScoreCard title="야간진료 운영 병원" content={nightHosp} onOpen={() => handleModalData('night')}
                           color="purple" imgSrc='night' />
                <ScoreCard title="일요일/공휴일 진료" content={holidayHosp} onOpen={() => handleModalData('holiday')}
                           color="orange" imgSrc='holiday'/>
                <ScoreCard title="필수의료 운영 병원" content={coreHosp} onOpen={() => handleModalData('core')}
                           color="red" imgSrc='emergency'/>
              </div>
              <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title={modalTitle} data={modalData} isLoading={isLoading}
                      currentPage={currentPage} totalPages={totalPages} onPageChange={pageChange}/>
              <div className='xl:col-span-4 row-span-2 flex xl:flex-col flex-row min-h-0 gap-4 col-span-12'>
                <div className='flex-1 min-h-75'>
                  <Dashboard title="병원 유형별 통계" series={categoryData.series} labels={categoryData.labels} type="donut" />
                </div>
                <div className='flex-1 min-h-75'>
                  <Dashboard title='진료 과목별 통계' series={deptData.series} labels={deptData.labels} type="bar"/>
                </div>
              </div>
              <div className='xl:col-span-8 min-h-0 p-5 bg-white rounded-2xl border border-gray-200 shadow-sm flex flex-col col-span-12'>
                <div className='flex mb-3 gap-4 p-2'>
                  <SelectBox label='시도' options={sidoList} value={selectedSido} sidoChange={handleSidoChange}/>
                  <SelectBox label='시군구' options={sggList} value={selectedSgg} sidoChange={setSelectedSgg}/>
                </div>
                <div className='relative flex-1 min-h-125'>
                  {selectedSido || selectedSgg ? <button className='absolute bg-blue-500 hover:bg-blue-400 bottom-6 left-1/2 -translate-x-1/2 z-20 text-white rounded-full px-5 py-2 shadow-sm transition-all cursor-pointer shadow'
                                                          onClick={handleInit}>🔄 초기화</button> : ''}
                  <KakaoMap selectedSido={selectedSido} selectedSgg={selectedSgg} onDetailClick={handleDetailView}
                            markers={displayMarker} onBoundsChange={handleBoundsChange} onZoomChange={fetchHospLocation} />
                </div>
              </div>
          </div>
        </main>
      </div>
    </div>
  );
}



