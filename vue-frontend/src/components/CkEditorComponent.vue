<template>
  <ckeditor
    v-if="editor && config"
    v-model="text"
    :editor="editor"
    :config="config"
    @ready="onReady"
  />
</template>

<script setup>
import {computed, ref, onMounted, useTemplateRef, watch} from 'vue';
import { Ckeditor, useCKEditorCloud } from '@ckeditor/ckeditor5-vue';

import 'ckeditor5/ckeditor5.css';

const LICENSE_KEY =
    'eyJhbGciOiJFUzI1NiJ9.eyJleHAiOjE3Njc4MzAzOTksImp0aSI6ImJkMjZlOTU4LTViODUtNDQ2Zi1hYzkyLTU0ZThmMzBjNGJjYSIsImxpY2Vuc2VkSG9zdHMiOlsiMTI3LjAuMC4xIiwibG9jYWxob3N0IiwiMTkyLjE2OC4qLioiLCIxMC4qLiouKiIsIjE3Mi4qLiouKiIsIioudGVzdCIsIioubG9jYWxob3N0IiwiKi5sb2NhbCJdLCJ1c2FnZUVuZHBvaW50IjoiaHR0cHM6Ly9wcm94eS1ldmVudC5ja2VkaXRvci5jb20iLCJkaXN0cmlidXRpb25DaGFubmVsIjpbImNsb3VkIiwiZHJ1cGFsIl0sImxpY2Vuc2VUeXBlIjoiZGV2ZWxvcG1lbnQiLCJmZWF0dXJlcyI6WyJEUlVQIl0sInZjIjoiZTE3YzlkZGIifQ.fn8JpI7hTcIal6Enuyd_2rpMunob4ORANmDiuMtQfvPiaFTfbiAroS2D3PPEMmPtKqINQF8z-LVk64iq2xZ4Lg';
const editorMenuBar = useTemplateRef('editorMenuBarElement');
const editorWordCount = useTemplateRef('editorWordCountElement');

const cloud = useCKEditorCloud({ version: '44.2.0', translations: ['ko'] });

const isLayoutReady = ref(false);


const editor = computed(() => {
  if (!cloud.data.value) {
    return null;
  }

  return cloud.data.value.CKEditor.ClassicEditor;
});

const props = defineProps({
  detail: {
    type: String,
    required: true,
  },
});

// 2) 부모로 detail 변경 사항을 알리기 위한 emits 설정
const emits = defineEmits(['update:detail']);

// 5) detail을 자식에서 바로 수정하지 않고, ref로 관리
const text = ref(props.detail);
// 6) CKEditor에서 text가 변경될 때마다, 부모에게 알림

watch(text, (newVal) => {
  emits('update:detail', newVal);

});
watch(
    () => props.detail,
    (newVal) => {
      text.value = newVal; // 부모 props가 바뀌면 text에 재할당
    }
);

const config = computed(() => {
  if (!isLayoutReady.value) {
    return null;
  }

  if (!cloud.data.value) {
    return null;
  }

  const {
    Alignment,
    AutoLink,
    Autosave,
    BalloonToolbar,
    BlockQuote,
    Bold,
    Bookmark,
    Code,
    CodeBlock,
    Essentials,
    FontBackgroundColor,
    FontColor,
    FontFamily,
    FontSize,
    Heading,
    Highlight,
    HorizontalLine,
    Indent,
    IndentBlock,
    Italic,
    Link,
    Paragraph,
    RemoveFormat,
    Strikethrough,
    Subscript,
    Superscript,
    Table,
    TableCaption,
    TableCellProperties,
    TableColumnResize,
    TableProperties,
    TableToolbar,
    TextPartLanguage,
    Title,
    Underline,
    WordCount
  } = cloud.data.value.CKEditor;

  return {
    toolbar: {
      items: [
        'textPartLanguage',
        '|',
        'heading',
        '|',
        'fontSize',
        'fontFamily',
        'fontColor',
        'fontBackgroundColor',
        '|',
        'bold',
        'italic',
        'underline',
        'strikethrough',
        'subscript',
        'superscript',
        'code',
        'removeFormat',
        '|',
        'horizontalLine',
        'link',
        'bookmark',
        'insertTable',
        'highlight',
        'blockQuote',
        'codeBlock',
        '|',
        'alignment',
        '|',
        'outdent',
        'indent'
      ],
      shouldNotGroupWhenFull: true
    },
    plugins: [
      Alignment,
      AutoLink,
      Autosave,
      BalloonToolbar,
      BlockQuote,
      Bold,
      Bookmark,
      Code,
      CodeBlock,
      Essentials,
      FontBackgroundColor,
      FontColor,
      FontFamily,
      FontSize,
      Heading,
      Highlight,
      HorizontalLine,
      Indent,
      IndentBlock,
      Italic,
      Link,
      Paragraph,
      RemoveFormat,
      Strikethrough,
      Subscript,
      Superscript,
      Table,
      TableCaption,
      TableCellProperties,
      TableColumnResize,
      TableProperties,
      TableToolbar,
      TextPartLanguage,
      Title,
      Underline,
      WordCount
    ],
    balloonToolbar: ['bold', 'italic', '|', 'link'],
    fontFamily: {
      supportAllValues: true
    },
    fontSize: {
      options: [10, 12, 14, 'default', 18, 20, 22],
      supportAllValues: true
    },
    heading: {
      options: [
        {
          model: 'paragraph',
          title: 'Paragraph',
          class: 'ck-heading_paragraph'
        },
        {
          model: 'heading1',
          view: 'h1',
          title: 'Heading 1',
          class: 'ck-heading_heading1'
        },
        {
          model: 'heading2',
          view: 'h2',
          title: 'Heading 2',
          class: 'ck-heading_heading2'
        },
        {
          model: 'heading3',
          view: 'h3',
          title: 'Heading 3',
          class: 'ck-heading_heading3'
        },
        {
          model: 'heading4',
          view: 'h4',
          title: 'Heading 4',
          class: 'ck-heading_heading4'
        },
        {
          model: 'heading5',
          view: 'h5',
          title: 'Heading 5',
          class: 'ck-heading_heading5'
        },
        {
          model: 'heading6',
          view: 'h6',
          title: 'Heading 6',
          class: 'ck-heading_heading6'
        }
      ]
    },
    language: 'ko',

    licenseKey: LICENSE_KEY,
    link: {
      addTargetToExternalLinks: true,
      defaultProtocol: 'https://',
      decorators: {
        toggleDownloadable: {
          mode: 'manual',
          label: 'Downloadable',
          attributes: {
            download: 'file'
          }
        }
      }
    },
    menuBar: {
      isVisible: true
    },
    placeholder: 'Type or paste your content here!',
    table: {
      contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties']
    }
  };
});

onMounted(() => {
  isLayoutReady.value = true;
});

function onReady(editor) {
  [...editorWordCount.value.children].forEach(child => child.remove());

  [...editorMenuBar.value.children].forEach(child => child.remove());

  const wordCount = editor.plugins.get('WordCount');
  editorWordCount.value.appendChild(wordCount.wordCountContainer);

  editorMenuBar.value.appendChild(editor.ui.view.menuBarView.element);
}
</script>
