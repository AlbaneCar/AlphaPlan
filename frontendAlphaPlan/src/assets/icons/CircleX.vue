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
    <svg xmlns="http://www.w3.org/2000/svg" :width="props.width" :height="props.width" viewBox="0 0 24 24" fill="none"
        :stroke="props.color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
        class="lucide lucide-circle-x">
        <circle cx="12" cy="12" r="10" />
        <path d="m15 9-6 6" />
        <path d="m9 9 6 6" />
    </svg>
</template>