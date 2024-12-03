<template>
    <div class="flex h-6 my-2">
        <div class="outline outline-1 outline-gray-200 rounded">
            <button @click.prevent="decrement"
                class="relative items-center justify-center w-10 h-full bg-gray-200 rounded-l font-bold z-0">-</button>
            <input type="text" :value="percentageWithSuffix" @input="updateValue"
                class="relative w-20 text-center z-10">
            <button @click.prevent="increment"
                class="relative items-center justify-center w-10 h-full bg-gray-200 rounded-r z-0">+</button>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, watch, defineProps, defineEmits } from 'vue';



const props = defineProps({
    modelValue: {
        type: Number,
        default: 0
    },
    percentageMode: {
        type: Boolean,
        default: true
    }
});

const modelValue = ref(props.modelValue);
const percentageMode = ref(props.percentageMode);
const value = ref(props.modelValue);
const emits = defineEmits(['update:modelValue']);

watch(() => props.modelValue, (newVal) => {
    value.value = newVal;
});

const percentageWithSuffix = computed(() => {
    return props.percentageMode ? value.value + '%' : value.value;
});

const increment = () => {
    if (props.percentageMode) {
        if (value.value < 100) {
            value.value = Math.min(value.value + 5, 100);
        }
    } else {
        if (value.value < 20) {
            value.value++;
        }
    }
    emits('update:modelValue', value.value);
};

const decrement = () => {
    if (props.percentageMode) {
        if (value.value > 0) {
            value.value = Math.max(value.value - 5, 0);
        }
    } else {
        if (value.value > 0) {
            value.value--;
        }
    }
    emits('update:modelValue', value.value);
};

const updateValue = (event) => {
    let inputValue = event.target.value;

    if (inputValue === '') {
        value.value = props.percentageMode ? 0 : 0;
        return;
    }

    let parsedValue = parseInt(inputValue);

    if (!isNaN(parsedValue)) {
        if (props.percentageMode) {
            value.value = Math.min(Math.max(Math.round(parsedValue / 5) * 5, 0), 100);
        } else {
            value.value = Math.min(Math.max(parsedValue, 0), 20);
        }
    }
    emits('update:modelValue', value.value);
};

</script>
