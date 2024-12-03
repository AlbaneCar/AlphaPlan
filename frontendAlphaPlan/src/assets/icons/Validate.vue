<template>
  <svg xmlns="http://www.w3.org/2000/svg" :width="props.width" :height="props.width" viewBox="0 0 24 24" fill="none" :stroke="props.color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-check"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg>
</template>

<script setup>
const props = defineProps({
    color: String,
    width: Number
})
</script>
