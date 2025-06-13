<template>
  <div class="tags-input">
    <input
      v-model="inputValue"
      class="form-control"
      placeholder="입력후 엔터"
      type="text"
      @keyup.enter.stop.prevent="handleAddTag"
    >
    <div
      v-for="tag in tags"
      :key="tag"
      class="tag"
    >
      {{ tag }}
      <button
        type="button"
        @click="handleRemoveTag(tag)"
      >
        X
      </button>
    </div>
  </div>
</template>

<script setup>
import {ref, watch} from 'vue';

// `update:tags` 이벤트를 발생시키는 `emit` 함수 정의
const emit = defineEmits(['update:tags']);

const props = defineProps({
  initialTags: {
    type: Array,
    default: () => []
  }
});

const tags = ref([...props.initialTags]);
const inputValue = ref('');

const handleAddTag = () => {
  console.log("handleAddTag");
  const newTag = inputValue.value.trim();
  if (newTag && !tags.value.includes(newTag)) {
    tags.value.push(newTag);
    inputValue.value = '';
    emit('update:tags', [...tags.value]);  // Spread 연산자를 사용하여 새 배열을 생성
  }
};

const handleRemoveTag = (tag) => {
  console.log("handleRemoveTag");
  if (tags.value.includes(tag)) {  // 삭제할 태그가 배열에 존재하는지 체크
    const newTags = tags.value.filter(t => t !== tag);
    tags.value = newTags;
    emit('update:tags', [...newTags]);  // Spread 연산자를 사용하여 새 배열을 생성
  }
};

watch(() => props.initialTags, (newTags) => {
  tags.value = newTags;
}, {deep: true});
</script>

<style scoped>
.tags-input {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.tag {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  background-color: #efefef;
  border-radius: 16px;
}

.tag button {
  border: none;
  background: none;
  cursor: pointer;
}
</style>
