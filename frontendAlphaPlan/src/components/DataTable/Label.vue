<script setup>

import { defineProps, ref } from 'vue';

const props = defineProps({
    type: {
        type: Number,
        default: 0,
    },
});


const colors_bg = ["bg-pink-100", "bg-blue-100", "bg-orange-100", "bg-purple-100", "bg-red-100"];
const colors_text = ["text-pink-600", "text-blue-600", "text-orange-600", "text-purple-600", "text-red-600"];
const colors_border = ["border-pink-400", "border-blue-400", "border-orange-400", "border-purple-400", "border-red-400"];

</script>

<template>
    <div :class="`${colors_bg[props.type]} ${colors_text[props.type]} ${colors_border[props.type]} rounded-md  text-center w-fit px-2 border select-none`">
        <slot />
    </div>
    
</template>