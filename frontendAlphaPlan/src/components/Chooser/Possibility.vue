<script setup>

const props = defineProps({
    active: Boolean
});

</script>

<template>
    <div :class="['flex', 'w-fit', 'p-1', 'px-2', 'rounded-md', 'font-CASlalomBold', 'text-xs', 'uppercase', { 'bg-white': !active, 'bg-custom-lightblueAP': active }, {
        'text-gray-500': !active, 'text-white': active
    }, { 'shadow-sm': active}]">
        <slot></slot>
    </div>
</template>