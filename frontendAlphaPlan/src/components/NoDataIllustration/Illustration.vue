<script setup>
defineProps({
    nameImage: String,
    width: Number,
})
</script>

<template>
    <img :src="`/${nameImage}`" :width="`${width}`" :height="`${width}`" alt="SVG" draggable="false">
</template>