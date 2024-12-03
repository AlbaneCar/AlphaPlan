<template>
    <div class="absolute flex flex-col justify-center items-center w-1/2 h-1/2 top-1/4 left-1/4">
        <div class=" flex flex-col justify-center items-center max-w-fit">
            <Illustration :nameImage="path" :width="500"/>
            <Title class="mt-5 w-[500px]">{{ message }}</Title>
        </div>
    </div>
</template>

<script setup>
import { defineProps, ref } from 'vue';
import Illustration from './Illustration.vue';
import Title from '../Title.vue';

const props = defineProps({
    path: String,
    message: String
});
</script>