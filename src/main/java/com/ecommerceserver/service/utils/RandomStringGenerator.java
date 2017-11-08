package com.ecommerceserver.service.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public final class RandomStringGenerator
{

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    private boolean useLower;
    private boolean useUpper;
    private boolean useDigits;
    private boolean usePunctuation;

    public RandomStringGenerator(RandomStringGeneratorBuilder builder)
    {
        this.useLower = builder.useLower;
        this.useUpper = builder.useUpper;
        this.useDigits = builder.useDigits;
        this.usePunctuation = builder.usePunctuation;
    }

    public static class RandomStringGeneratorBuilder
    {

        private boolean useLower;
        private boolean useUpper;
        private boolean useDigits;
        private boolean usePunctuation;

        public RandomStringGeneratorBuilder()
        {
            this.useLower = false;
            this.useUpper = false;
            this.useDigits = false;
            this.usePunctuation = false;
        }

        public RandomStringGeneratorBuilder useLower(boolean useLower)
        {
            this.useLower = useLower;
            return this;
        }

        public RandomStringGeneratorBuilder useUpper(boolean useUpper)
        {
            this.useUpper = useUpper;
            return this;
        }

        public RandomStringGeneratorBuilder useDigits(boolean useDigits)
        {
            this.useDigits = useDigits;
            return this;
        }

        public RandomStringGeneratorBuilder usePunctuation(boolean usePunctuation)
        {
            this.usePunctuation = usePunctuation;
            return this;
        }

        public RandomStringGenerator build()
        {
            return new RandomStringGenerator(this);
        }
    }

    public String generate(int length)
    {
        // Argument Validation.
        if (length <= 0)
        {
            return "";
        }

        // Variables.
        StringBuilder password = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        random.setSeed(System.nanoTime());
        // Collect the categories to use.
        List<String> charCategories = new ArrayList<>(4);
        if (useLower)
        {
            charCategories.add(LOWER);
        }
        if (useUpper)
        {
            charCategories.add(UPPER);
        }
        if (useDigits)
        {
            charCategories.add(DIGITS);
        }
        if (usePunctuation)
        {
            charCategories.add(PUNCTUATION);
        }

        // Build the password.
        for (int i = 0; i < length; i++)
        {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return new String(password);
    }
}