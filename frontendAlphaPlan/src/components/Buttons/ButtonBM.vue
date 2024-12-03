<script setup>
import { PencilLine } from 'lucide-vue-next';
import { defineProps } from 'vue';
import { watch } from 'vue';
import { ref } from 'vue';
import { defineEmits } from 'vue';

const props = defineProps({
    valeur: Object,
    studentId : Number
});

const emits = defineEmits(['open']);


const valeur = ref(props.valeur != null ? props.valeur.note : 0);
const isPositive = ref(valeur.value > 0);
const isNull = ref(valeur.value == 0);
const data = ref(props.valeur);

watch(() => props.valeur, (newValue) => {
    const note = newValue != null ? newValue.note : 0;
    isPositive.value = note > 0;
    isNull.value = note == 0;
    valeur.value = note;
    data.value = newValue;
});

</script>

<template>
    <div class="flex justify-center items-center">
        <div :class="{
            'border-[1px] border-gray-200 w-20 h-7 rounded-tl-md rounded-bl-md flex items-center justify-center text-custom-lightblueAP font-CASlalomRegular bg-white font-semibold': isPositive,
            'border-[1px] border-gray-200 w-20 h-7 rounded-tl-md rounded-bl-md flex items-center justify-center text-red-500 font-CASlalomRegular bg-white font-semibold': !isPositive && !isNull,
            'border-[1px] border-gray-200 w-20 h-7 rounded-tl-md rounded-bl-md flex items-center justify-center text-custom-darkblueAP font-CASlalomRegular bg-white font-semibold': isNull
        }">
            <div v-if="isPositive">
            +{{ valeur }}
            </div>
            <div v-else>
            {{ valeur }}
            </div>
        </div>
        <div id="OpenDialogButton" class="border-[1px] border-gray-200 bg-gray-200 w-8 h-7 rounded-tr-md rounded-br-md flex items-center justify-center hover:bg-gray-300 hover:border-gray-300 cursor-pointer" @click="() => $emit('open', studentId, data)">
            <PencilLine class="w-4 h-4 text-custom-darkblueAP"/>
        </div>
    </div>
</template>