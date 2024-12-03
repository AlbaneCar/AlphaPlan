<script setup>
import { Calendar } from 'lucide-vue-next';

const props = defineProps({
    date : String,
})

function formatDate(date) {
    const dateObject = new Date(date);
    const day = dateObject.getDate() < 10 ? '0' + dateObject.getDate() : dateObject.getDate();
    const month = dateObject.getMonth() + 1 < 10 ? '0' + (dateObject.getMonth() + 1) : dateObject.getMonth() + 1;
    const year = dateObject.getFullYear();
    return `${day}/${month}/${year}`;
}

</script>

<template>
    <div class="flex justify-start items-center gap-2 shadow-sm rounded-md px-1 bg-custom-sideBarBG border border-gray-200">
        <p>{{ formatDate(props.date) }}</p>
    </div>

</template>