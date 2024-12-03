<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    width: {
        type: String,
        default: '24'
    },
    color: {
        type: String,
        default: 'currentColor'
    }
});
</script>

<template>
    <svg xmlns="http://www.w3.org/2000/svg" :width="props.width" :height="props.width" viewBox="0 0 24 24" fill="none"
        :stroke="props.color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
        class="lucide lucide-cloud-upload">
        <path d="M4 14.899A7 7 0 1 1 15.71 8h1.79a4.5 4.5 0 0 1 2.5 8.242" />
        <path d="M12 12v9" />
        <path d="m16 16-4-4-4 4" />
    </svg>
</template>