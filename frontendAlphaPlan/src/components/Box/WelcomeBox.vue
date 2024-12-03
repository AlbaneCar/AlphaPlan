<script setup>
import Title from '../Title.vue';
import Illustration from '../NoDataIllustration/Illustration.vue';
</script>

<template>
    <div class="flex rounded-md justify-between items-center bg-blue-100 w-full py-10 px-10 mt-10">
        <Title>Bienvenue Richard !</Title>
        <div class="">
            <Illustration src="work.svg" width="250" class="absolute -top-1 right-16"/>
        </div>
    </div>
</template>