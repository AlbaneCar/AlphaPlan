<script setup>

const props = defineProps({
    width: {
        type: Number,
        default: 24
    },
    color: {
        type: String,
        default: 'currentColor'
    }
});
</script>

<template>
    <svg xmlns="http://www.w3.org/2000/svg" :width="props.width" :height="props.width" viewBox="0 0 24 24" fill="none" :stroke="props.color"
        stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-plus">
        <path d="M5 12h14" />
        <path d="M12 5v14" />
    </svg>
</template>