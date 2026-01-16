'use client';
import { ApexOptions } from "apexcharts";
import ReactApexChart from "react-apexcharts";

interface DashboardProps {
  title?: string,
  series: number[] | { name: string; data: number[] }[],
  labels?: string[],
  type: 'donut' | 'pie' | 'bar' | 'line';
}

export default function Dashboard({title, series, labels, type}: DashboardProps) {
  const formattedSeries = (type === 'donut' || type === 'pie') ? series as number[] : series as [{name: string, data: number[]}];
  const options: ApexOptions = {
    chart: {
      type: type,
      toolbar: { show: false },
    },
    plotOptions: {
      bar: {
        horizontal: true,
        borderRadius: 8,
        borderRadiusApplication: 'end',
        barHeight: '60%',
        distributed: true,
        dataLabels: {
          position: 'center',
        },
      },
    },
    dataLabels: {
      enabled: true,
      style: { fontSize:'12px', fontFamily: 'nanumGothicCoding' }
    },
    xaxis: {
      categories: labels,
      labels: { show: false },
      axisBorder: { show: true },
      axisTicks: { show: true },
    },
    yaxis: {
      labels: {
        maxWidth: 70,
        style: { fontSize: '12px', fontWeight: 500 }
      }
    },
    grid: {
      show: true,
      xaxis: { lines: { show: true } }
    },
    colors: ['#1e88e5', '#3949ab', '#0097a7', '#546e7a', '#78909c'],
    legend: { show: false },
    labels: labels,
  }

  return (
    <div className="bg-white p-5 border border-gray-200 rounded-2xl shadow-sm h-full">
       <ReactApexChart options={options} series={formattedSeries} type={type} height={350} />
       <h3 className="text-sm text-center font-medium">{title}</h3>
    </div>
  );
}