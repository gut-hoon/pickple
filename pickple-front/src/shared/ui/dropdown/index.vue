<template>
  <div class="dropdown-wrapper" :style="{ maxWidth: maxWidth }">
    <button
      @click="toggleDropdown"
      :disabled="disabled"
      class="dropdown-button cursor-pointer"
      :class="{ 'dropdown-button-disabled': disabled }"
    >
      <span class="dropdown-button-text">{{ displayText }}</span>
      <i
        :class="['fas', isOpen ? 'fa-chevron-up' : 'fa-chevron-down']"
        class="dropdown-icon"
      ></i>
    </button>

    <div v-if="isOpen" class="dropdown-menu" :style="menuStyle">
      <button
        v-for="item in items"
        :key="item.id"
        @click="selectItem(item.id)"
        :class="{
          'dropdown-item-active': modelValue === item.id,
          'dropdown-item-inactive': modelValue !== item.id
        }"
        class="dropdown-item cursor-pointer"
      >
        {{ item.name }}
      </button>
    </div>

    <div
      v-if="isOpen"
      class="dropdown-overlay"
      @click="closeDropdown"
    ></div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  modelValue: {
    type: [String, Number],
    required: true
  },
  items: {
    type: Array,
    required: true
  },
  placeholder: {
    type: String,
    default: '선택해주세요'
  },
  disabled: {
    type: Boolean,
    default: false
  },
  maxWidth: {
    type: String,
    default: '300px'
  },
  showBorder: {
    type: Boolean,
    default: true
  },
  containerClass: {
    type: String,
    default: ''
  },
  menuMaxHeight: {
    type: String,
    default: '300px'
  }
});

const emit = defineEmits(['update:modelValue', 'change']);

const isOpen = ref(false);

const selectedItemName = computed(() => {
  const item = props.items.find(i => i.id === props.modelValue);
  return item ? item.name : '';
});

const displayText = computed(() => {
  return selectedItemName.value || props.placeholder;
});

const containerStyle = computed(() => ({
  padding: '1rem',
  borderBottom: props.showBorder ? '1px solid #E5E7EB' : 'none'
}));

const menuStyle = computed(() => ({
  maxHeight: props.menuMaxHeight,
  overflowY: 'auto'
}));

const toggleDropdown = () => {
  if (!props.disabled) {
    isOpen.value = !isOpen.value;
  }
};

const closeDropdown = () => {
  isOpen.value = false;
};

const selectItem = (itemId) => {
  emit('update:modelValue', itemId);
  emit('change', itemId);
  closeDropdown();
};
</script>

<style scoped>
/* 드롭다운 컨테이너 */
.dropdown-container {
  position: relative;
}

.dropdown-wrapper {
  position: relative;
  width: 100%;
}

/* 드롭다운 버튼 */
.dropdown-button {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1rem;
  background-color: var(--white);
  border: 1px solid #D1D5DB;
  border-radius: var(--border-radius);
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--primary-dark);
  transition: all 0.2s ease;
}

.dropdown-button:hover:not(.dropdown-button-disabled) {
  border-color: var(--primary);
  background-color: #F9FAFB;
}

.dropdown-button:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
}

.dropdown-button-disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background-color: #F3F4F6;
}

.dropdown-button-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-icon {
  margin-left: 0.5rem;
  font-size: 0.75rem;
  transition: transform 0.2s ease;
  flex-shrink: 0;
}

/* 드롭다운 메뉴 */
.dropdown-menu {
  position: absolute;
  top: calc(100% + 0.5rem);
  left: 0;
  right: 0;
  background-color: var(--white);
  border: 1px solid #E5E7EB;
  border-radius: var(--border-radius);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  z-index: 50;
  animation: slideDown 0.2s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 드롭다운 아이템 */
.dropdown-item {
  width: 100%;
  padding: 0.75rem 1rem;
  text-align: left;
  font-size: 0.875rem;
  border: none;
  background-color: transparent;
  transition: background-color 0.15s ease;
}

.dropdown-item:hover {
  background-color: #F3F4F6;
}

.dropdown-item-active {
  background-color: var(--primary);
  color: var(--primary-dark);
  font-weight: 600;
}

.dropdown-item-inactive {
  color: var(--primary-dark);
}

/* 오버레이 */
.dropdown-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 40;
  background-color: transparent;
}

/* 스크롤바 스타일 */
.dropdown-menu::-webkit-scrollbar {
  width: 6px;
}

.dropdown-menu::-webkit-scrollbar-track {
  background: #F3F4F6;
  border-radius: 3px;
}

.dropdown-menu::-webkit-scrollbar-thumb {
  background: #D1D5DB;
  border-radius: 3px;
}

.dropdown-menu::-webkit-scrollbar-thumb:hover {
  background: #9CA3AF;
}
</style>
