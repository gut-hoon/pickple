<!--
  DatePicker 컴포넌트

  Props:
    - label (String, optional): 입력 필드의 레이블
    - modelValue (String): 선택된 날짜 (YYYY-MM-DD 형식)
    - placeholder (String): placeholder 텍스트
    - error (String, optional): 에러 메시지

  Emits:
    - update:modelValue: 날짜가 선택될 때 발생

  사용 예시:
    <script setup>
    import { ref } from 'vue';
    import { DatePicker } from '@/shared/ui';

    const birthDate = ref('');
    </script>

    <template>
      <DatePicker
        label="생일"
        v-model="birthDate"
        placeholder="날짜를 선택하세요"
        error="생일을 입력해주세요"
      />
    </template>
-->

<template>
  <div class="date-picker">
    <label v-if="label" class="date-picker-label">{{ label }}</label>

    <div class="date-picker-input-wrapper">
      <input
        :value="displayValue"
        type="text"
        :placeholder="placeholder"
        :class="['date-picker-input', { 'date-picker-input--error': error }]"
        @click="toggleCalendar"
        readonly
      />
      <i class="fas fa-calendar date-picker-icon" @click="toggleCalendar"></i>
    </div>

    <span v-if="error" class="date-picker-error">{{ error }}</span>

    <!-- 백드롭 -->
    <transition name="backdrop-fade">
      <div v-if="showCalendar" class="calendar-backdrop" @click="showCalendar = false"></div>
    </transition>

    <!-- 캘린더 드롭다운 -->
    <transition name="calendar-fade">
      <div v-if="showCalendar" class="calendar-dropdown" @click.stop>
        <!-- 헤더: 년도/월 선택 -->
        <div class="calendar-header">
          <button class="calendar-nav-button" @click="previousMonth">
            <i class="fas fa-chevron-left"></i>
          </button>

          <div class="calendar-title">
            <button
              class="calendar-select-button"
              @click="showYearPicker = !showYearPicker; showMonthPicker = false"
            >
              {{ currentYear }}년
            </button>
            <button
              class="calendar-select-button"
              @click="showMonthPicker = !showMonthPicker; showYearPicker = false"
            >
              {{ currentMonth + 1 }}월
            </button>
          </div>

          <button class="calendar-nav-button" @click="nextMonth">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>

        <!-- 연도 선택 -->
        <div v-if="showYearPicker" class="calendar-content">
          <div ref="yearPickerListRef" class="calendar-picker-list">
            <button
              v-for="year in yearOptions"
              :key="year"
              :ref="el => { if (year === currentYear) yearButtonRef = el }"
              :class="[
                'calendar-picker-item',
                { 'calendar-picker-item--selected': year === currentYear }
              ]"
              @click="selectYear(year)"
            >
              {{ year }}
            </button>
          </div>
        </div>

        <!-- 월 선택 -->
        <div v-else-if="showMonthPicker" class="calendar-content">
          <div class="calendar-picker-list">
            <button
              v-for="month in 12"
              :key="month"
              :class="[
                'calendar-picker-item',
                {
                  'calendar-picker-item--selected': month - 1 === currentMonth,
                  'calendar-picker-item--disabled': isMonthDisabled(month - 1)
                }
              ]"
              @click="selectMonth(month - 1)"
              :disabled="isMonthDisabled(month - 1)"
            >
              {{ month }}
            </button>
          </div>
        </div>

        <!-- 날짜 그리드 -->
        <div v-else class="calendar-content">
          <!-- 요일 헤더 -->
          <div class="calendar-weekdays">
            <div v-for="day in weekdays" :key="day" class="calendar-weekday">
              {{ day }}
            </div>
          </div>

          <!-- 날짜 그리드 -->
          <transition :name="`slide-${slideDirection}`" mode="out-in">
            <div class="calendar-grid" :key="`${currentYear}-${currentMonth}`">
              <button
                v-for="date in calendarDates"
                :key="date.key"
                :class="[
                  'calendar-date',
                  {
                    'calendar-date--other-month': date.isOtherMonth,
                    'calendar-date--today': date.isToday,
                    'calendar-date--selected': date.isSelected,
                    'calendar-date--disabled': date.isFutureDate
                  }
                ]"
                @click="selectDate(date)"
                :disabled="date.isOtherMonth || date.isFutureDate"
              >
                {{ date.day }}
              </button>
            </div>
          </transition>
        </div>

        <!-- 확인/취소 버튼 -->
        <div class="calendar-footer">
          <template v-if="showYearPicker || showMonthPicker">
            <button
              class="calendar-confirm-button calendar-confirm-button--right"
              @click="showYearPicker ? (showYearPicker = false) : (showMonthPicker = false)"
            >
              확인
            </button>
          </template>
          <template v-else>
            <button class="calendar-cancel-button" @click="cancelSelection">
              취소
            </button>
            <button class="calendar-confirm-button" @click="confirmSelection">
              확인
            </button>
          </template>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue';
import { formatDate, getNormalizedDate } from '@/shared/lib';

const props = defineProps({
  label: {
    type: String,
    default: ''
  },
  modelValue: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: '날짜를 선택하세요'
  },
  error: {
    type: String,
    default: ''
  },
  allowFuture: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:modelValue']);


/**
 * 상태 관리
 */
const showCalendar = ref(false);
const showYearPicker = ref(false);
const showMonthPicker = ref(false);

const currentYear = ref(new Date().getFullYear());
const currentMonth = ref(new Date().getMonth());

const slideDirection = ref('next');

const selectedDateInCalendar = ref('');

const yearPickerListRef = ref(null);
const yearButtonRef = ref(null);

// Form에 표시할 값
const displayValue = computed(() => {
  if (!props.modelValue) return '';
  return formatDate(props.modelValue);
});


/**
 * 옵션 생성
 * - 연, 월, 일 선택 가능한 옵션 생성
 */
const weekdays = ['일', '월', '화', '수', '목', '금', '토'];

const yearOptions = computed(() => {
  const currentYearValue = new Date().getFullYear();
  const years = [];
  const endYear = props.allowFuture ? currentYearValue + 10 : currentYearValue;
  for (let i = currentYearValue - 100; i <= endYear; i++) {
    years.push(i);
  }
  return years;
});

const calendarDates = computed(() => {
  const dates = [];
  const firstDay = new Date(currentYear.value, currentMonth.value, 1);
  const lastDay = new Date(currentYear.value, currentMonth.value + 1, 0);
  const startDayOfWeek = firstDay.getDay();

  // 이전 달의 빈 칸
  for (let i = 0; i < startDayOfWeek; i++) {
    dates.push(createEmptyDateCell(`empty-prev-${i}`));
  }

  // 현재 달의 날짜들
  const today = getNormalizedDate(new Date());
  const selectedDate = selectedDateInCalendar.value ? getNormalizedDate(new Date(selectedDateInCalendar.value)) : null;

  for (let day = 1; day <= lastDay.getDate(); day++) {
    const date = getNormalizedDate(new Date(currentYear.value, currentMonth.value, day));
    const isFutureDate = !props.allowFuture && date.getTime() > today.getTime();

    dates.push({
      day,
      date,
      isOtherMonth: false,
      isFutureDate,
      isToday: date.getTime() === today.getTime(),
      isSelected: selectedDate ? date.getTime() === selectedDate.getTime() : false,
      key: `current-${day}`
    });
  }

  // 다음 달의 빈 칸 (총 42칸을 채우기 위해)
  const remainingDays = 42 - dates.length;
  for (let i = 0; i < remainingDays; i++) {
    dates.push(createEmptyDateCell(`empty-next-${i}`));
  }

  return dates;
});

// 그리드를 채우기 위한 빈 셀
const createEmptyDateCell = (key) => ({
  day: '',
  date: null,
  isOtherMonth: true,
  isToday: false,
  isSelected: false,
  key
});


/**
 * 캘린더 액션
 * - 캘린더 열기
 * - 선택 확인/취소 및 캘린더 닫기
 */
const toggleCalendar = () => {
  showCalendar.value = !showCalendar.value;

  if (showCalendar.value) {
    const date = props.modelValue ? new Date(props.modelValue) : new Date();
    currentYear.value = date.getFullYear();
    currentMonth.value = date.getMonth();
    selectedDateInCalendar.value = props.modelValue || '';
    showYearPicker.value = false;
    showMonthPicker.value = false;
  }
};

const confirmSelection = () => {
  if (selectedDateInCalendar.value) {
    emit('update:modelValue', selectedDateInCalendar.value);
  }
  showCalendar.value = false;
};

const cancelSelection = () => {
  showCalendar.value = false;
};


/**
 * 연도 선택
 */
const isYearDisabled = (year) => {
  if (props.allowFuture) {
    return false;
  }

  const currentYearValue = new Date().getFullYear();
  return year > currentYearValue;
};

const selectYear = (year) => {
  if (isYearDisabled(year)) {
    return;
  }
  currentYear.value = year;
};

// 연도 선택 오픈 시 현재 선택된 연도로 스크롤
watch(showYearPicker, async (isOpen) => {
  if (isOpen) {
    await nextTick();
    if (yearButtonRef.value && yearPickerListRef.value) {
      yearButtonRef.value.scrollIntoView({ block: 'center', behavior: 'smooth' });
    }
  }
});


/**
 * 월 선택
 */
const isMonthDisabled = (month) => {
  if (props.allowFuture) {
    return false;
  }

  const today = new Date();
  const currentYearValue = today.getFullYear();
  const currentMonthValue = today.getMonth();

  if (currentYear.value > currentYearValue) {
    return true;
  }

  if (currentYear.value === currentYearValue && month > currentMonthValue) {
    return true;
  }

  return false;
};

const selectMonth = (month) => {
  if (isMonthDisabled(month)) {
    return;
  }
  currentMonth.value = month;
};


/**
 * 월 이동 네비게이션
 */
const previousMonth = () => {
  slideDirection.value = 'prev';
  if (currentMonth.value === 0) {
    currentMonth.value = 11;
    currentYear.value--;
  } else {
    currentMonth.value--;
  }
};

const nextMonth = () => {
  slideDirection.value = 'next';
  if (currentMonth.value === 11) {
    currentMonth.value = 0;
    currentYear.value++;
  } else {
    currentMonth.value++;
  }
};


/**
 * 날짜 선택
 */
const selectDate = (dateObj) => {
  // 빈 칸이나 다른 월, 연도의 날짜를 선택할 때 반응하지 않도록
  if (dateObj.isOtherMonth || dateObj.isFutureDate) {
    return;
  }

  const year = currentYear.value;
  const month = String(currentMonth.value + 1).padStart(2, '0');
  const day = String(dateObj.day).padStart(2, '0');

  selectedDateInCalendar.value = `${year}-${month}-${day}`;
};


// Esc 버튼으로 탈출 가능
const handleKeyDown = (event) => {
  if (event.key === 'Escape' && showCalendar.value) {
    showCalendar.value = false;
  }
};


/**
 * 라이프사이클
 */
onMounted(() => {
  document.addEventListener('keydown', handleKeyDown);
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeyDown);
});
</script>

<style scoped>
.date-picker {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.date-picker-label {
  font-size: 16px;
  font-weight: 600;
  color: var(--black);
}

.date-picker-input-wrapper {
  position: relative;
}

.date-picker-input {
  width: 100%;
  padding: 1rem;
  padding-right: 3rem;
  background-color: var(--light);
  border: 1px solid var(--gray-300, #e5e7eb);
  border-radius: var(--border-radius);
  font-family: var(--font-family);
  font-size: 16px;
  color: var(--black);
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
  cursor: pointer;
}

.date-picker-input:focus {
  outline: none;
  border-color: var(--primary);
}

.date-picker-input--error {
  border-color: var(--error);
}

.date-picker-input--error:focus {
  border-color: var(--error);
}

.date-picker-icon {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--medium-gray);
  font-size: 18px;
  cursor: pointer;
}

.date-picker-error {
  font-size: 14px;
  color: var(--error);
  margin-top: -0.25rem;
}

/* 백드롭 */
.calendar-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

/* 캘린더 드롭다운 */
.calendar-dropdown {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 280px;
  max-width: calc(100vw - 2rem);
  max-height: calc(100vh - 2rem);
  z-index: 1000;
  background: var(--white);
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

/* 캘린더 콘텐츠 영역 */
.calendar-content {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  height: 250px;
  min-height: 0;
  margin-top: 1rem;
}

/* 캘린더 헤더 */
.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
  flex-shrink: 0;
}

.calendar-nav-button {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  border-radius: 50%;
  color: var(--black);
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.calendar-nav-button:hover {
  background-color: var(--gray);
}

.calendar-nav-button:active {
  background-color: var(--gray);
}

.calendar-nav-button:focus {
  outline: none;
  background: transparent;
}

.calendar-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.calendar-select-button {
  padding: 0.5rem 1rem;
  border: 1px solid var(--gray-300, #e5e7eb);
  border-radius: var(--border-radius);
  font-family: var(--font-family);
  font-size: 14px;
  font-weight: 600;
  color: var(--black);
  background-color: var(--light);
  cursor: pointer;
  transition: all 0.2s;
}

.calendar-select-button:hover {
  border-color: var(--primary);
  background-color: var(--primary-light);
}

/* 연도/월 선택 리스트 */
.calendar-picker-list {
  overflow-y: auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.5rem;
  padding: 0.5rem 0;
  align-content: start;
  height: 100%;
}

.calendar-picker-item {
  padding: 0.75rem;
  border: none;
  border-radius: var(--border-radius);
  font-family: var(--font-family);
  font-size: 14px;
  font-weight: 500;
  color: var(--black);
  background-color: transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.calendar-picker-item:hover {
  background-color: var(--primary-light);
}

.calendar-picker-item--selected {
  background-color: var(--primary);
  color: var(--white);
  font-weight: 600;
}

.calendar-picker-item--disabled {
  color: var(--medium-gray);
  cursor: not-allowed;
  pointer-events: none;
  opacity: 0.5;
}

/* 캘린더 푸터 */
.calendar-footer {
  display: flex;
  gap: 0.5rem;
  padding-top: 0.5rem;
  flex-shrink: 0;
}

.calendar-cancel-button {
  flex: 1;
  padding: 0.75rem;
  background-color: var(--white);
  border: 1px solid var(--gray-300, #e5e7eb);
  border-radius: var(--border-radius);
  color: var(--black);
  font-family: var(--font-family);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.calendar-cancel-button:hover {
  background-color: var(--gray);
  border-color: var(--medium-gray);
}

.calendar-confirm-button {
  flex: 1;
  padding: 0.75rem;
  background-color: var(--primary);
  border: none;
  border-radius: var(--border-radius);
  color: var(--white);
  font-family: var(--font-family);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

.calendar-confirm-button:hover {
  background-color: var(--primary-dark);
}

.calendar-confirm-button:disabled {
  background-color: var(--gray-300, #e5e7eb);
  color: var(--medium-gray);
  cursor: not-allowed;
}

.calendar-confirm-button--right {
  flex: none;
  margin-left: auto;
  width: calc((100% - 0.5rem) / 2);
}

/* 요일 헤더 */
.calendar-weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.25rem;
  text-align: center;
}

.calendar-weekday {
  font-size: 11px;
  font-weight: 600;
  color: var(--medium-gray);
  padding: 0.3rem 0;
}

/* 날짜 그리드 */
.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.25rem;
}

.calendar-date {
  aspect-ratio: 1;
  width: 100%;
  max-height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 50%;
  font-size: 14px;
  font-weight: 500;
  color: var(--black);
  background-color: transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.calendar-date:hover:not(:disabled) {
  background-color: var(--primary-light);
}

.calendar-date--other-month {
  visibility: hidden;
  cursor: default;
  pointer-events: none;
}

.calendar-date--disabled {
  color: var(--medium-gray);
  cursor: not-allowed;
  pointer-events: none;
  opacity: 0.5;
}

.calendar-date--today {
  font-weight: 700;
  color: var(--primary-dark);
  background-color: var(--primary-light);
}

.calendar-date--selected {
  background-color: var(--primary);
  color: var(--white);
  font-weight: 700;
}

/* 애니메이션 */
.calendar-fade-enter-active,
.calendar-fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.calendar-fade-enter-from {
  opacity: 0;
  transform: translate(-50%, -50%) scale(0.95);
}

.calendar-fade-enter-to {
  opacity: 1;
  transform: translate(-50%, -50%) scale(1);
}

.calendar-fade-leave-from {
  opacity: 1;
  transform: translate(-50%, -50%) scale(1);
}

.calendar-fade-leave-to {
  opacity: 0;
  transform: translate(-50%, -50%) scale(0.95);
}

/* 백드롭 애니메이션 */
.backdrop-fade-enter-active,
.backdrop-fade-leave-active {
  transition: opacity 0.3s ease;
}

.backdrop-fade-enter-from,
.backdrop-fade-leave-to {
  opacity: 0;
}

/* 월 이동 슬라이드 애니메이션 */
.slide-next-enter-active,
.slide-next-leave-active,
.slide-prev-enter-active,
.slide-prev-leave-active {
  transition: transform 0.15s cubic-bezier(0.25, 0.46, 0.45, 0.94), opacity 0.15s ease-out;
}

/* 다음 달로 이동 (왼쪽으로 슬라이드) */
.slide-next-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.slide-next-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}

/* 이전 달로 이동 (오른쪽으로 슬라이드) */
.slide-prev-enter-from {
  transform: translateX(-100%);
  opacity: 0;
}

.slide-prev-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
</style>
