import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { toast } from "sonner";

import RenderFormField from '@/components/shared/RenderFormField';
import { Form } from '@/components/ui/form';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '@/components/ui/button';
import { useMutation } from "@tanstack/react-query";
import authApi from "@/apis/auth.api";
import _omit from "lodash/omit";


const SignUp = () => {

  const SignUpSchema = z.object({
    email: z
      .string()
      .refine((val) => /\S+@\S+\.\S+/.test(val), "Email không hợp lệ"),
    password: z.string().min(5, "Mật khẩu phải có ít nhất 5 ký tự"),
    confirmPassword: z.string().min(5, "Mật khẩu phải có ít nhất 5 ký tự"),
  });

  const SignUpSchemaWithConfirm = SignUpSchema.refine(
    (data) => data.password === data.confirmPassword,
    {
      message: "Passwords do not match",
      path: ["confirmPassword"],
    }
  );

  type SignUpType = z.infer<typeof SignUpSchemaWithConfirm>;

  const form = useForm<SignUpType>({
    resolver: zodResolver(SignUpSchemaWithConfirm),
  });

  const signUpAccountMutation = useMutation({
    mutationFn: (body: Omit<SignUpType, 'confirmPassword'>) => authApi.signup(body)
  })

  const submit = async (data: SignUpType) => {
    const body = _omit(data, ['confirmPassword'])
    try {
      const result = await signUpAccountMutation.mutateAsync(body, {
        onSuccess: data => {
          toast.success(data.data.message)
        }
      }) 
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <section className="py-8">
      <div className="max-w-xl w-full mx-auto">
        <Card className="w-full">
          <CardHeader>
            <CardTitle className="text-xl">Sign Up</CardTitle>
            <CardDescription>
              Enter your information to create an account
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(submit)}
                className="grid gap-4 text-left"
                noValidate
              >
                <div className="grid gap-2 ">
                  <RenderFormField
                    name="email"
                    title="Email"
                    type="input"
                    inputType="email"
                    placeholder="abc@gmail.com"
                    control={form.control}
                  />
                </div>
                <div className="grid gap-2">
                  <RenderFormField
                    name="password"
                    title="Password"
                    type="input"
                    inputType="password"
                    control={form.control}
                  />
                </div>
                <div className="grid gap-2">
                  <RenderFormField
                    name="confirmPassword"
                    title="Confirm Password"
                    type="input"
                    inputType="password"
                    control={form.control}
                  />
                </div>
                <div className=" flex items-center gap-2 ">
                  <Button className="w-1/2" type="reset" variant="outline">
                    Reset
                  </Button>
                  <Button type="submit" className="w-1/2">
                    Sign Up
                  </Button>
                </div>
              </form>
            </Form>


            <div className="mt-4 text-center text-sm">
              Already have an account?{" "}
              <Link to="/sign-in" className="underline">
                Sign in
              </Link>
            </div>
          </CardContent>
        </Card>
      </div>
    </section>
  );
};

export default SignUp;
