<script setup>
import ProgressSpinner from 'primevue/progressspinner';
</script>

<template>
    <div class="flex justify-center items-center">
        <ProgressSpinner style="width: 30px; height: 30px" strokeWidth="8" fill="transparent" animationDuration="3s"
            aria-label="Custom ProgressSpinner" />
    </div>

</template>

<style scoped>
:deep(.p-progress-spinner .p-progress-circle) {
    stroke: white !important;
}
</style>